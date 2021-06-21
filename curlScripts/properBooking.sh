echo "GET --> /ticketBooking"
echo
echo
echo
echo
curl http://localhost:9000/ticketBooking -v -o ./curlScripts/properOutputs/ticketBookingInit.txt &
echo
echo
echo
echo
echo "POST (with redirection) --> /chooseDateAndTime"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d 'dateTime=2021-06-15T21:00' http://localhost:9000/chooseDateAndTime -v > ./curlScripts/properOutputs/chooseDateAndTime.txt &
echo
echo
echo
echo
echo "GET --> /availableMovies"
echo
echo
echo
echo
sleep 15
curl -b ./curlScripts/cookiesProper.txt http://localhost:9000/availableMovies -v > ./curlScripts/properOutputs/availableMovies.txt &
echo
echo
echo
echo
echo "/POST (with redirection) --> chooseScreening"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d 'title=Mortal Kombat' -d 'startTime=2021-06-26T20:00' http://localhost:9000/chooseScreening -v > ./curlScripts/properOutputs/chooseScreening.txt &
echo
echo
echo
echo
echo "GET --> /availableSeats"
echo
echo
echo
echo
sleep 15
curl -b ./curlScripts/cookiesProper.txt http://localhost:9000/availableSeats -v > ./curlScripts/properOutputs/availableSeats.txt &
echo
echo
echo
echo
echo "POST (with redirection) --> /chooseSeats"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d 'rowNumber=10' -d 'seatNumber=1,2' http://localhost:9000/chooseSeats -v > ./curlScripts/properOutputs/chooseSeats.txt &
echo
echo
echo
echo
echo "GET --> /availableTickets"
echo
echo
echo
echo
sleep 15
curl -b ./curlScripts/cookiesProper.txt http://localhost:9000/availableTickets -v > ./curlScripts/properOutputs/availableTickets.txt &
echo
echo
echo
echo
echo "POST (with redirection) --> /chooseTicketTypes"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d 'ticketType=adult' -d 'ticketType=adult'  http://localhost:9000/chooseTicketTypes -v > ./curlScripts/properOutputs/chooseTicketTypes.txt &
echo
echo
echo
echo
echo "GET --> /customerData"
echo
echo
echo
echo
sleep 15
curl -b ./curlScripts/cookiesProper.txt http://localhost:9000/customerData -v > ./curlScripts/properOutputs/customerData.txt &
echo
echo
echo
echo
echo "POST (with redirection) --> /createCustomer  --  redirects to /reservationSummary"
echo "Because POST (with redirection) --> /createCustomer  --  redirects to /reservationSummary and in fact makes a real reservation:"
echo "- in current script i will not call GET on /reservationSummary, because it would try to make the same reservation again and"
echo "- finally it would cause an error so this script would finish with an error before calling the last endpoint"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d 'name=Łukasz' -d 'surname=Bambo-Mambo'  http://localhost:9000/createCustomer -v > ./curlScripts/properOutputs/createCustomer.txt &
echo
echo
echo
echo
echo "POST (with redirection) --> /finishSession"
echo
echo
echo
echo
sleep 15
curl -L -c ./curlScripts/cookiesProper.txt -d "finish=" http://localhost:9000/finishSession -v > ./curlScripts/properOutputs/finishSession.txt
