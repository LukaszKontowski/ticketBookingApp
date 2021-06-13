package models

case class Customer(name: String, surname: String) {
  def isPersonalDataValid: Boolean = isNameValid && isSurnameValid

  def isNameValid: Boolean = "[A-Z[ĄĆĘŁŃÓŚŹŻ]]{1}[a-z[ąćęłńóśźż]]{2,}".r.matches(this.name)

  def isSurnameValid: Boolean =
    if (this.surname.indexOf('-') == -1)
      "[A-Z[ĄĆĘŁŃÓŚŹŻ]]{1}[a-z[ąćęłńóśźż]]{2,}".r.matches(this.surname)
    else
      "[A-Z[ĄĆĘŁŃÓŚŹŻ]]{1}[a-z[ąćęłńóśźż]]{2,}-[A-Z[ĄĆĘŁŃÓŚŹŻ]]{1}[a-z[ąćęłńóśźż]]{2,}".r.matches(this.surname)

  override def toString: String = s"Mr/Ms $name $surname"

}
