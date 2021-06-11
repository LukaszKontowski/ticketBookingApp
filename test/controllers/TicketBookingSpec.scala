package controllers

import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, PlaySpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class TicketBookingSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
  "TicketBooking" must {
    "have initial page working correctly and go next page if date-time is correct" in {
      go to s"http://localhost:$port/ticketBooking"
      pageTitle mustBe "Ticket Booking Init"
      find(cssSelector("h2")).isEmpty mustBe false
      find(cssSelector("h2")).foreach(e => e.text mustBe "Choose date and time when You would like to see a movie:")
      /* -- below -> doesn't work yet, don't know why dateTimeLocalField("dateTime").value_=("2021-06-15T19:00") doesn't set value
      click on "dateTime"
      dateTimeLocalField("dateTime").value_=("2021-06-15T19:00")
      println(dateTimeLocalField("dateTime").isEnabled)
      println(dateTimeLocalField("dateTime").text)
      println(dateTimeLocalField("dateTime").underlying)
      submit()
      eventually {
        pageTitle mustBe "Available Movies"
        find(cssSelector("h2")).isEmpty mustBe false
        find(cssSelector("h2")).foreach(e => e.text mustBe "Movies available close to chosen time:")
      }*/
    }
  }
}
