package controllers

import models._

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Forms._

import java.time.LocalDateTime

@Singleton
class TicketBooking @Inject() (cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  private var availableScreenings: List[Screening] = Nil
  private var chosenScreeningOption: Option[Screening] = None
  private var chosenSeats: Seq[(Int, Int)] = Nil
  private var chosenTickets: List[Ticket] = Nil

  val customerDataForm: Form[Customer] = Form(mapping(
    "name" -> text(3),
    "surname" -> text(3))(Customer.apply)(Customer.unapply))

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
    val chosenDateTime = LocalDateTime.parse(request.session.get("dateTime").get)
    availableScreenings = DefaultScreenings(chosenDateTime).availableScreenings
    Ok(views.html.availableMovies(availableScreenings))
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
}