package models

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import scala.annotation.tailrec

object DefaultScreenings {

  private val mortalKombat: Movie = Movie("Mortal Kombat", 110)
  private val fastAndFurious: Movie = Movie("Fast And Furious", 120)
  private val ultimateRapBattle: Movie = Movie("Ultimate Rap Battle", 75)
  private val tomAndJerry: Movie = Movie("Tom And Jerry", 90)
  private val goodGirls: Movie = Movie("Good Girls", 100)

  private val screeningRoom1 = ScreeningRoom(1)
  private val screeningRoom2 = ScreeningRoom(2)
  private val screeningRoom3 = ScreeningRoom(3)
  private val screeningRoom4 = ScreeningRoom(4)
  private val screeningRoom5 = ScreeningRoom(5)

  private val from = LocalDateTime.now().minusDays(1).withHour(12).withMinute(0).truncatedTo(ChronoUnit.MINUTES)
  private val to = LocalDateTime.now().plusDays(7).withHour(12).withMinute(0).truncatedTo(ChronoUnit.MINUTES)

  private var mutableScreenings: List[Screening] = Nil

  def getAvailableScreenings: List[Screening] = {
    if (mutableScreenings.isEmpty) {
      mutableScreenings = {
        fillRoomWithScreenings(screeningRoom1, mortalKombat, from, to, Nil).sortBy(_.startTime) ++
          fillRoomWithScreenings(screeningRoom2, fastAndFurious, from, to, Nil).sortBy(_.startTime) ++
          fillRoomWithScreenings(screeningRoom3, ultimateRapBattle, from, to, Nil).sortBy(_.startTime) ++
          fillRoomWithScreenings(screeningRoom4, tomAndJerry, from, to, Nil).sortBy(_.startTime) ++
          fillRoomWithScreenings(screeningRoom5, goodGirls, from, to, Nil).sortBy(_.startTime)
      }
      for (screening <- mutableScreenings) screening.initializeScreeningRoomPlaces()
    }
    mutableScreenings
  }

  @tailrec
  private def fillRoomWithScreenings(screeningRoom: ScreeningRoom,
                                     movie: Movie,
                                     from: LocalDateTime,
                                     to: LocalDateTime,
                                     screenings: List[Screening]): List[Screening] = {
    if (from.isAfter(to))
      screenings
    else
      fillRoomWithScreenings(screeningRoom, movie, from.plusMinutes(movie.durationInMinutes + 15), to,
        Screening(movie, screeningRoom, from) :: screenings)
  }
}
