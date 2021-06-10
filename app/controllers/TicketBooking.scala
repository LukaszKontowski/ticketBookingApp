package controllers

import models._

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Forms._

import scala.util.{Try,Success,Failure}
import java.time.LocalDateTime

@Singleton
class TicketBooking @Inject() (cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {

  private var availableScreenings: List[Screening] = Nil
  private var chosenScreeningOption: Option[Screening] = None
  private var chosenSeats: Seq[(Int, Int)] = Nil
  private var chosenTickets: Seq[Ticket] = Nil
  private var reservationOption: Option[Reservation] = None

  val customerDataForm: Form[Customer] = Form(mapping(
    "name" -> text(3),
    "surname" -> text(3)
  )(Customer.apply)(Customer.unapply))

  def ticketBookingInit: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.ticketBookingInit())
  }

  def chooseDateAndTime: Action[AnyContent] = Action { implicit request =>
    val postVals = request.body.asFormUrlEncoded
    postVals.map { args =>
      if (args("dateTime").head.nonEmpty) {
        val chosenDateTime = args("dateTime").head
        Redirect(routes.TicketBooking.availableMovies).withSession("dateTime" -> chosenDateTime)
      } else {
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
        availableScreenings = DefaultScreenings(dateTime).availableScreenings
        Ok(views.html.availableMovies(availableScreenings))
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
    Ok(views.html.availableSeats(chosenScreeningOption.get))
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
    Ok(views.html.availableTickets(chosenScreeningOption.get, chosenSeats))
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
      chosenTickets = rowSeatType.map{ case ((row, seat), ticketType) => Ticket(row, seat, ticketType) }
      Redirect(routes.TicketBooking.customerData)
        .withSession(request.session)
    }.getOrElse(Redirect(routes.TicketBooking.ticketBookingInit).withNewSession)
  }

  def customerData: Action[AnyContent] = Action { implicit request =>
    Ok(views.html.customerData(customerDataForm, chosenScreeningOption.get, chosenTickets))
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
    Ok("all ok")
  }
}