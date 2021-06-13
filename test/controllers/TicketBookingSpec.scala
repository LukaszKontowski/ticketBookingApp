package controllers

import org.openqa.selenium.Keys
import org.scalatestplus.play.{HtmlUnitFactory, OneBrowserPerSuite, PlaySpec}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite

class TicketBookingSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
  "TicketBooking" must {
    "have initial page working correctly and go next page if date-time is correct" in {
      go to s"http://localhost:$port/ticketBooking"
      pageTitle mustBe "Ticket Booking Init"
      find(cssSelector("h2")).isEmpty mustBe false
      find(cssSelector("h2")).foreach(e => e.text mustBe "Choose date and time when You would like to see a movie:")
      click on "dateTime"
      dateTimeLocalField("dateTime").value_=("06162021" + Keys.TAB + "0700PM")
      /*val dateTimeLocalField = chromeDriver.findElement(By.xpath("//form//input[@name='dateTime']"))
      dateTimeLocalField.sendKeys("06162021")
      dateTimeLocalField.sendKeys(Keys.TAB)
      dateTimeLocalField.sendKeys("0700PM")*/
      submit()
      eventually {
        pageTitle mustBe "Available Movies"
        find(cssSelector("h2")).isEmpty mustBe false
        find(cssSelector("h2")).foreach(e => e.text mustBe "Movies available close to chosen time:")
      }
    }
  }
}
