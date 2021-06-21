package models

case class Movie(title: String, durationInMinutes: Int) {
  override def toString: String = s"Movie: \"$title\" <-> duration in minutes: $durationInMinutes"
}
