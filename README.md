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
  
5) Execute run.sh script that would build and run this app for you:  
./run.sh  
  
6) When the app's server is already up and running, there are at least two options to check it out:  
    a) You may check this app like a real user in your browser at this url (initial page):
       http://localhost:9000/ticketBooking  
  
    b) You may also check it using command line tools - e.g. curl to see requests and responses in action.  
