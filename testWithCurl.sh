curl http://localhost:9000/ticketBooking -v
curl -L -c cookies.txt -d 'dateTime=2021-06-15T21:00' http://localhost:9000/chooseDateAndTime
curl -b cookies.txt http://localhost:9000/availableMovies
curl -L -c cookies.txt -d 'title=Mortal Kombat' -d 'startTime=2021-06-15T21:10' http://localhost:9000/chooseScreening
curl -b cookies.txt http://localhost:9000/availableSeats
curl -L -c cookies.txt -d 'title=Mortal Kombat' -d 'startTime=2021-06-15T21:10' http://localhost:9000/chooseScreening
