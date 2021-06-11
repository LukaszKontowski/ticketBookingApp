package models

import java.time.LocalDateTime

case class Screening(movie: Movie, screeningRoom: ScreeningRoom, startTime: LocalDateTime) {

  def screeningRoomPlaces: Array[Array[String]] = realScreeningRoomPlaces
  
  private val realScreeningRoomPlaces: Array[Array[String]] = Array.ofDim[String](11, 10)

  def makeReservation(tickets: Seq[Ticket]): Unit =
    for (ticket <- tickets) {
      realScreeningRoomPlaces(ticket.rowNumber).update(ticket.seatNumberInRow, "BUSY")
      println("one place reserved")
    }

  def initializeScreeningRoomPlaces(): Unit = {
    realScreeningRoomPlaces.update(0, Array("Nr__1", "Nr__2", "Nr__3", "Nr___4", "Nr__5", "Nr__6", "Nr__7", "Nr___8", "Nr__9", "Nr__10"))
    realScreeningRoomPlaces.update(1, Array.fill(10)("BUSY"))
    realScreeningRoomPlaces.update(2, Array("BUSY", "BUSY", "BUSY", "FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "FREE."))
    realScreeningRoomPlaces.update(3, Array.fill(10)("BUSY"))
    realScreeningRoomPlaces.update(4, Array("FREE.", "FREE.", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY", "FREE.", "FREE."))
    realScreeningRoomPlaces.update(5, Array.fill(10)("BUSY"))
    realScreeningRoomPlaces.update(6, Array("FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY"))
    realScreeningRoomPlaces.update(7, Array.fill(10)("BUSY"))
    realScreeningRoomPlaces.update(8, Array.fill(10)("FREE."))
    realScreeningRoomPlaces.update(9, Array.fill(10)("FREE."))
    realScreeningRoomPlaces.update(10, Array.fill(10)("FREE."))
  }


  /*def currentPlacesViewAsString: String =
    (for (arr <- screeningRoomPlaces) yield (arr.mkString + "\n")).mkString.init*/

  override def toString: String =
    s"$movie <-> $screeningRoom <-> starts at: $startTime"

}
