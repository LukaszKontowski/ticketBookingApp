package models

case class Ticket(rowNumber: Int, seatNumberInRow: Int, ticketType: TicketType)

abstract class TicketType {
  val price: Double
}

class ChildTicket(override val price: Double = 12.5) extends TicketType {
  override def toString: String = "Child Ticket"
}

class StudentTicket(override val price: Double = 18.0) extends TicketType{
  override def toString: String = "Student Ticket"
}

class AdultTicket(override val price: Double = 25.0) extends TicketType {
  override def toString: String = "Adult Ticket"
}