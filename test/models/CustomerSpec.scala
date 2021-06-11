package models

import org.scalatestplus.play.PlaySpec

class CustomerSpec extends PlaySpec {

  "Customer.isNameValid" must {
    "return true if given name is valid" in {
      Customer("Łukasz", "whatever").isNameValid mustBe (true)
    }

    "return false if given name starts with lowercase character" in {
      Customer("łukasz", "whatever").isNameValid mustBe (false)
    }

    "return false if given name contains any non-letter character" in {
      Customer("łuka2sz", "whatever").isNameValid mustBe (false)
    }

    "return false if given name contains upper-case characters other than first letter" in {
      Customer("ŁuKasz", "whatever").isNameValid mustBe (false)
    }

    "return false if given name is too short" in {
      Customer("Łu", "whatever").isNameValid mustBe (false)
    }
  }

  "Customer.isSurnameValid" must {
    "return true if given surname is valid" in {
      Customer("Łukasz", "Kałuża").isSurnameValid mustBe (true)
    }

    "return true if given surname is valid and containing two parts" in {
      Customer("Łukasz", "Kałuża-Bambino").isSurnameValid mustBe (true)
    }

    "return false if given surname starts with lowercase character" in {
      Customer("Łukasz", "whatever").isSurnameValid mustBe (false)
    }

    "return false if given surname contains any non-letter character other than -" in {
      Customer("Łukasz", "Kałuż3a").isSurnameValid mustBe (false)
    }

    "return false if given surname contains upper-case characters other than first letter of 1st or 2nd surname" in {
      Customer("Łukasz", "BaMbo").isSurnameValid mustBe (false)
    }

    "return false if given surname is too short" in {
      Customer("Łukasz", "Ba").isSurnameValid mustBe (false)
    }
  }

}
