System Requirement:
  - Java8 compatible    

Guide to run the game:
 - Go to ManacalaApplication class and run the application
 - Once service is up and running Go to browser and type below URL to see the list of mancala api:
   http://localhost:8087/swagger-ui/index.html
 - In case of any issues with the application running port kindly update below property in application.properties for desired port:
   server.port={provide the desired port number}
  
API Details:
 - There are two endpoints in Swagger-UI
   - /mancala/board/numberOfPits/{numberOfPits}/numberOfStones/{numberOfStones}
        - This Api will generate Game unique id which needs to be used as an input to mancala/play API
           
   
         
   - /mancala/play
        - This API is to start playing the game by providing game's unique id and choosen pit, state of game will be
           mainitained using the game unique id 
          
          
 


