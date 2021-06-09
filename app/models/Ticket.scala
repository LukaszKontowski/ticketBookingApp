package models

case class Ticket(rowNumber: Int, seatNumberInRow: Int, ticketType: TicketType)

abstract class TicketType(price: Double) {
  def getPrice: Double = price
}

case class ChildTicket(price: Double = 12.5) extends TicketType(price: Double) {
  override def toString: String = "Child Ticket"
}

case class StudentTicket(price: Double = 18.0) extends TicketType(price: Double) {
  override def toString: String = "Student Ticket"
}

case class AdultTicket(price: Double = 25.0) extends TicketType(price: Double) {
  override def toString: String = "Adult Ticket"
}