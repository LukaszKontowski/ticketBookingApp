package models

import org.scalatestplus.play.PlaySpec

import java.time.LocalDateTime

class ScreeningSpec extends PlaySpec {
  private val movie = Movie("xxx", 120)
  private val room = ScreeningRoom(1)
  private val startTime = LocalDateTime.now()
  private val tickets = Seq(Ticket(10,1,new AdultTicket()), Ticket(10,2,new AdultTicket()))

  "Screening" must {
    "have default screeningRoomPlaces as an Array[Array[String]] filled with nulls before init" in {
      Screening(movie, room, startTime).screeningRoomPlaces
        .forall(arr => arr.forall(_ == null)) mustBe (true)
    }

    "have screeningRoomPlaces filled with FREE. or BUSY values only after init" in {
      val scr = Screening(movie, room, startTime)
      scr.initializeScreeningRoomPlaces()
      scr.screeningRoomPlaces.tail
        .forall(arr => arr.forall { s: String => (s == "FREE." || s == "BUSY") }) mustBe (true)
    }

    "have exactly 46 free seats available after init" in {
      val scr = Screening(movie, room, startTime)
      scr.initializeScreeningRoomPlaces()
      scr.screeningRoomPlaces.tail.flatten.count(_ == "FREE.") mustBe (46)
    }

    "change the seat string value from FREE. to BUSY after reservation is made" in {
      val scr = Screening(movie, room, startTime)
      scr.initializeScreeningRoomPlaces()
      (scr.screeningRoomPlaces(10)(1), scr.screeningRoomPlaces(10)(2)) mustBe ("FREE.", "FREE.")
      scr.makeReservation(tickets)
      (scr.screeningRoomPlaces(10)(1), scr.screeningRoomPlaces(10)(2)) mustBe ("BUSY", "BUSY")
    }
  }

}
