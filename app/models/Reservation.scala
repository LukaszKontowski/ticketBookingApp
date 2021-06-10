package models

case class Reservation(screening: Screening, customer: Customer, tickets: List[Ticket]) {
  val totalPrice: Double = tickets.map(_.ticketType.price).sum

  def makeReservation: Unit =
    for (ticket <- this.tickets) {
      this.screening.screeningRoomPlaces(ticket.rowNumber)(ticket.seatNumberInRow) = "BUSY"
    }

  val isValid: Boolean = {
    this.tickets.nonEmpty &&
    this.tickets.forall(ticket =>
            this.screening.screeningRoomPlaces(ticket.rowNumber)(ticket.seatNumberInRow) == "FREE.") &&
    this.arePlacesChosenCorrectly &&
    this.arePlacesNextToPreviouslyReserved
  }

  private def arePlacesChosenCorrectly: Boolean = {
    val ticketsGrouped = tickets.groupBy(_.rowNumber)
    ticketsGrouped.forall{ case (k, v) => 
      v.map(_.seatNumberInRow).sorted == (v.map(_.seatNumberInRow).min to v.map(_.seatNumberInRow).max).toList
    }
  }

  private def arePlacesNextToPreviouslyReserved: Boolean = {
    val ticketsGrouped = tickets.groupBy(_.rowNumber)
    ticketsGrouped.forall{ case (k, v) =>
      val ticketSeatNumbers = v.map(_.seatNumberInRow).sorted
      val previouslyReservedSeatNumbers = screening.screeningRoomPlaces(k).zipWithIndex.filter(_._1 == "BUSY").map(_._2)
      (ticketSeatNumbers ++ previouslyReservedSeatNumbers).sorted.toList ==
        ((ticketSeatNumbers ++ previouslyReservedSeatNumbers).sorted.min to (ticketSeatNumbers ++ previouslyReservedSeatNumbers).sorted.max).toList
    }
  }
}
