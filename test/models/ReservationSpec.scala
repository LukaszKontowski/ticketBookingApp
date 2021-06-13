package models

import org.scalatestplus.play.PlaySpec

import java.time.LocalDateTime

class ReservationSpec extends PlaySpec {
  private val movie = Movie("xxx", 120)
  private val room = ScreeningRoom(1)
  private val startTime = LocalDateTime.now()
  private val screening = Screening(movie, room, startTime)
  screening.initializeScreeningRoomPlaces()
  private val customer = Customer("Vito", "Bambino")

  "Reservation" must {
    "be valid if places were chosen correctly - 1 place chosen" in {
      val tickets = Seq(Ticket(10,1,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (true)
    }

    "be valid if places were chosen correctly - 2 or more places chosen" in {
      val tickets = Seq(Ticket(10,1,new AdultTicket()), Ticket(10,2,new AdultTicket()), Ticket(10,3,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (true)
    }

    "be valid if places were chosen correctly - 2 or more places chosen next to busy ones" in {
      val tickets = Seq(Ticket(4,8,new AdultTicket()), Ticket(4,9,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (true)
    }

    "be invalid if places were chosen incorrectly - 2 same places chosen" in {
      val tickets = Seq(Ticket(10,1,new AdultTicket()), Ticket(10,1,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (false)
    }

    "be invalid if places were chosen incorrectly - 2 free places chosen but not next to each other" in {
      val tickets = Seq(Ticket(10,1,new AdultTicket()), Ticket(10,3,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (false)
    }

    "be invalid if places were chosen incorrectly - 2 free places chosen but not next to previously reserved" in {
      val tickets = Seq(Ticket(2,5,new AdultTicket()), Ticket(2,6,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (false)
    }

    "be invalid if places were chosen incorrectly - 1 busy place chosen" in {
      val tickets = Seq(Ticket(1,1,new AdultTicket()))
      Reservation(screening, customer, tickets).isValid mustBe (false)
    }

    "be invalid if no places were chosen" in {
      val tickets = Nil
      Reservation(screening, customer, tickets).isValid mustBe (false)
    }
  }
}
