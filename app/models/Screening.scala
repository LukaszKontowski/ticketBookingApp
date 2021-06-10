package models

import java.time.LocalDateTime

case class Screening(movie: Movie, screeningRoom: ScreeningRoom, startTime: LocalDateTime) {
  private val r = scala.util.Random
  
  val screeningRoomPlaces: Array[Array[String]] =
    Array("Nr__1", "Nr__2", "Nr__3", "Nr___4", "Nr__5", "Nr__6", "Nr__7", "Nr___8", "Nr__9", "Nr__10") +:
      Array.fill(10)(Array.fill(10)("FREE."))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("BUSY", "BUSY", "BUSY", "FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "FREE."))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("FREE.", "FREE.", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY", "FREE.", "FREE."))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("FREE.", "FREE.", "FREE.", "FREE.", "FREE.", "BUSY", "BUSY", "BUSY", "BUSY", "BUSY"))


  def currentPlacesViewAsString: String =
    (for (arr <- screeningRoomPlaces) yield (arr.mkString + "\n")).mkString.init

  override def toString: String =
    s"$movie <-> $screeningRoom <-> starts at: $startTime"

}
