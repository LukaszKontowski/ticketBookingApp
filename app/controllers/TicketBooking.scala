package controllers

import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import play.filters.csrf.CSRF

import java.time.LocalDateTime
import javax.inject._
import scala.util.{Failure, Success, Try}

@Singleton
class TicketBooking @Inject() (cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  private val availableScreenings: List[Screening] = DefaultScreenings.getAvailableScreenings
  private var availableScreeningsFiltered: List[Screening] = Nil
  private var chosenScreeningOption: Option[Screening] = None
  private var chosenSeats: Seq[(Int, Int)] = Nil
  private var chosenTickets: Seq[Ticket] = Nil

  val customerDataForm: Form[Customer] = Form(mapping(
    "name" -> text(3),
    "surname" -> text(3)
  )(Customer.apply)(Customer.unapply))

  def ticketBookingInit: Action[AnyContent] = Action { implicit request =>
    val csrfToken = CSRF.getToken.get.value
    Ok(views.html.ticketBookingInit()).withSession("csrfToken" -> csrfToken)
  }

  def chooseDateAndTime: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      if (args("dateTime").head.nonEmpty) {
        val chosenDateTime = args("dateTime").head
        println(chosenDateTime)
        Redirect(routes.TicketBooking.availableMovies).withSession(request.session.+("dateTime" -> chosenDateTime))
      } else {
        println("empty date time")
        Redirect(routes.TicketBooking.ticketBookingInit).flashing("error" -> "choose date and time!")
      }
    }.getOrElse(Redirect(routes.TicketBooking.ticketBookingInit))
  }

  def availableMovies: Action[AnyContent] = Action { implicit request =>
    val chosenDateTimeTry = Try(LocalDateTime.parse(request.session.get("dateTime").get))
    chosenDateTimeTry match {
      case Failure(_) =>
        Redirect(routes.TicketBooking.ticketBookingInit)
          .withNewSession.flashing("error" -> "wrong date-time format")
      case Success(dateTime) =>
        availableScreeningsFiltered =
          availableScreenings.filter(screening =>
            screening.startTime.plusMinutes(90).isAfter(dateTime) &&
              screening.startTime.minusHours(3).isBefore(dateTime) &&
              screening.startTime.minusMinutes(15).isAfter(LocalDateTime.now())
          )
        Ok(views.html.availableMovies(availableScreeningsFiltered)).withSession(request.session)
    }
  }

  def chooseScreening: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val title = args("title").head
      val startTime = args("startTime").head
      chosenScreeningOption = availableScreenings
        .find(scr => scr.movie.title == title && scr.startTime == LocalDateTime.parse(startTime))
      chosenScreeningOption match {
        case None => Redirect(routes.TicketBooking.availableMovies).withSession(request.session)
        case Some(_) => Redirect(routes.TicketBooking.availableSeats).withSession(request.session)
      }
    }.getOrElse(Redirect(routes.TicketBooking.ticketBookingInit).withNewSession)
  }

  def availableSeats: Action[AnyContent] = Action { implicit request =>
    val screeningTry = Try(chosenScreeningOption.get)
    screeningTry match {
      case Failure(_) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
      case Success(screening) => Ok(views.html.availableSeats(screening)).withSession(request.session)
    }
  }

  def chooseSeats: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val rowNumberTry = Try(args("rowNumber").head.toInt)
      val seatNumbersTry = Try(args("seatNumber").head.split(",").toList.map(_.toInt))
      (rowNumberTry, seatNumbersTry) match {
        case (Success(rowNumber), Success(seatNumbers)) => {
          chosenSeats = for (seatNumber <- seatNumbers) yield (rowNumber, seatNumber)
          val dummyTickets = chosenSeats.toList.map { case (row, seat) => Ticket(row, seat - 1, new AdultTicket) }
          val dummyReservationTry = Try(Reservation(chosenScreeningOption.get, Customer("x", "y"), dummyTickets))
          dummyReservationTry match {
            case Failure(_) =>
              Redirect(routes.TicketBooking.availableSeats)
                .withSession(request.session)
                .flashing("error" -> "seats chosen incorrectly - try again and follow the rules!")
            case Success(dummyReservation) =>
              if (dummyReservation.isValid)
                Redirect(routes.TicketBooking.availableTickets).withSession(request.session)
              else
                Redirect(routes.TicketBooking.availableSeats)
                  .withSession(request.session)
                  .flashing("error" -> "seats chosen incorrectly - try again and follow the rules!")
          }
        }
        case _ => {
          Redirect(routes.TicketBooking.availableSeats)
            .withSession(request.session)
            .flashing("error" -> "values typed into text-boxes have incorrect format - read the instructions and try again!")
        }
      }
    }.getOrElse(Redirect(routes.TicketBooking.ticketBookingInit).withNewSession)
  }

  def availableTickets: Action[AnyContent] = Action { implicit request =>
    val screeningTry = Try(chosenScreeningOption.get)
    screeningTry match {
      case Failure(_) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
      case Success(screening) =>
        chosenSeats match {
          case Nil => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
          case x :: xs => Ok(views.html.availableTickets(screening, chosenSeats)).withSession(request.session)
        }
    }
  }

  def chooseTicketTypes: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      val ticketTypes = args("ticketType").map {
        case "child" => new ChildTicket
        case "student" => new StudentTicket
        case "adult" => new AdultTicket
      }
      val rowSeatType = chosenSeats.zip(ticketTypes)
      chosenTickets = rowSeatType.map{ case ((row, seat), ticketType) => Ticket(row, seat-1, ticketType) }
      Redirect(routes.TicketBooking.customerData)
        .withSession(request.session)
    }.getOrElse(Redirect(routes.TicketBooking.ticketBookingInit).withNewSession)
  }

  def customerData: Action[AnyContent] = Action { implicit request =>
    val screeningTry = Try(chosenScreeningOption.get)
    screeningTry match {
      case Failure(_) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
      case Success(screening) =>
        chosenTickets match {
          case Nil => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
          case x :: xs => Ok(views.html.customerData(customerDataForm, screening, chosenTickets)).withSession(request.session)
        }
    }
  }

  def createCustomer: Action[AnyContent] = Action { implicit request =>
    customerDataForm.bindFromRequest.fold(
      formWithErrors =>
        Redirect(routes.TicketBooking.customerData)
          .withSession(request.session)
          .flashing("error" -> "invalid name or surname - try again!"),
      correctForm => {
        val customer = correctForm
        if (customer.isPersonalDataValid)
          Redirect(routes.TicketBooking.reservationSummary)
            .withSession(request.session.+("name_surname" -> (customer.name + "_" + customer.surname)))
        else
          Redirect(routes.TicketBooking.customerData)
            .withSession(request.session)
            .flashing("error" -> "invalid name or surname - try again!")
      })
  }

  def reservationSummary: Action[AnyContent] = Action { implicit request =>
    val customerDataArrayTry = Try(request.session.get("name_surname").get.split("_"))
    customerDataArrayTry match {
      case Failure(_) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
      case Success(customerDataArray) =>
        Try(Customer(customerDataArray(0), customerDataArray(1))) match {
          case Failure(_) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
          case Success(customer) =>
            val reservationTry = Try(Reservation(chosenScreeningOption.get, customer, chosenTickets))
            reservationTry match {
              case Failure(msg) => Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
              case Success(reservation) =>
                if (reservation.isValid) {
                  reservation.screening.makeReservation(reservation.tickets)
                  Ok(views.html.reservationSummary(reservation)).withSession(request.session)
                } else {
                  Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
                }
            }
        }
    }
  }

  def finishSession: Action[AnyContent] = Action { implicit request =>
    chosenScreeningOption= None
    chosenSeats= Nil
    chosenTickets = Nil
    Redirect(routes.TicketBooking.ticketBookingInit).withNewSession
  }
}