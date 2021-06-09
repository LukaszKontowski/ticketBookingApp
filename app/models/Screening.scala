package models

import java.time.LocalDateTime

case class Screening(movie: Movie, screeningRoom: ScreeningRoom, startTime: LocalDateTime) {
  private val r = scala.util.Random
  
  val screeningRoomPlaces: Array[Array[String]] =
    Array("Nr__1", "Nr__2", "Nr_3", "Nr__4", "Nr__5", "Nr__6", "Nr_7", "Nr__8", "Nr__9", "Nr_10") +:
      Array.fill(10)(Array.fill(10)("FREE"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt, Array.fill(10)("BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("BUSY", "FREE", "BUSY", "BUSY", "FREE", "FREE", "BUSY", "BUSY", "FREE", "BUSY"))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("FREE", "BUSY", "BUSY", "FREE", "FREE", "FREE", "BUSY", "BUSY", "FREE", "FREE"))
        .updated((r.nextDouble() * 10).floor.toInt,
          Array("BUSY", "FREE", "FREE", "FREE", "FREE", "FREE", "BUSY", "BUSY", "BUSY", "BUSY"))


  def currentPlacesViewAsString: String =
    (for (arr <- screeningRoomPlaces) yield (arr.mkString + "\n")).mkString.init

  override def toString: String =
    s"$movie <-> $screeningRoom <-> starts at: $startTime"

}
