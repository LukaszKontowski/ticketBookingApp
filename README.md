# ticketBookingApp
ticket booking app for learning purposes  
  
In order to run this app on your machine follow these steps:  
  
1) First - make sure you have the Java 8 JDK (or Java 11 JDK) installed.  
java -version (Make sure you have version 1.8 or 11.)   
- if not - you might get official Oracle version or download from here: https://adoptopenjdk.net  
  
2) Make sure you have SBT installed. If not - get it from here: https://www.scala-sbt.org  
  
3) Get all App's sourcecode from my github. You might do it in your command line:  
git clone https://github.com/LukaszKontowski/ticketBookingApp.git  
  
4) In command line (terminal) change directory into ticket booking app's root directory:  
cd ./ticketbookingapp  
  
5) Make sure you can execute the run-scripts and then execute run.sh to build and run this app:  
chmod 777 ./curlScripts/properBooking.sh  
chmod 777 ./run.sh  
./run.sh  
  
6) When the app's server is already up and running, there are at least two options to check it out:  
   
    a) after running ./run.sh - this script will: (1) automatically open browser and (2) begin the shell script that would run whole use case calling respective endpoints (./curlScripts/properBooking.sh) - to see requests and responses in action -> this is a scenario, where everything is ok - data is valid and user goes from initial endpoint to the final one step-by-step. If You want to check out, how this app is handling invalid data or a user trying to get to some further endpoints in a tricky-way (forbidden) - you can check it out in browser or using curl.    
  
    b) You may check this app like a real user in your browser at this url (initial page):
       http://localhost:9000/ticketBooking  
  
    c) You may also check it using command line tools - e.g. curl to see requests and responses in action.   
  
7) If you want to stop the application - just hit ENTER in command line in the tab, where you did execute ./run.sh   
  
8) Note: after initializing ./run.sh script (demo) - it takes about 5 minutes to complete it. Please, be patient - i did set up such long amount of time with breaks inside just to make sure that all processes within this script are executed without errors and also for better readability when reading results from ./curlScripts/properBooking.sh  
  
  -> ./curlScripts/properBooking.sh - this script uses curl with -v option in each call - so part of the output containing html content goes into files generated by this script under ./curlScripts/properOutputs + part of the output containing http statuses and other important info about requests and responses - goes directly to the command-line output.  
  
9) Note: this app is an imitation of a real ticket-booking system and this particular program works in-memory only. So - the ticket reeservations are being saved and remembered as long as the Play-app server is up and running. If you stop the application like in point 7) or if it breaks - all the reservations will be lost - after reinitializing the application will create screenings with their screening-room places once again - each time you restart the app server all the previous reservations are lost.  
  
10) In this particular system the user can reserve one place or multiple seats within one reservation - but if he/she wants to reserve multiple seats - it is only possible within one row. It could be changed so that it would be possible to reserve on multiple rows within one reservation, but for now it is like it is (didn't have time for it).  
