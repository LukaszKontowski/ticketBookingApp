package models

case class ScreeningRoom(id: Int, numOfRows: Int = 10, numOfSeatsPerRow: Int = 10) {
  override def toString: String = s"Screening Room number $id"
}
