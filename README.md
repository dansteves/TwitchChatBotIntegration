# TwitchChatBotIntegration
Allows users to read and write text from/to IRC Twitch chat as a substitute for user input for some of my previous existing projects. Config.java contains the main method of the program and you must run three arguments: your twitch username, your oauth key, and the name of the game you are playing. Bot.java is where all of my existing commands are. Currently Bot.java at the moment is set up to add users to a text file that is loaded and saved each time the command is run if they are interested in entering a tournament I would like to host using the command !interest, and has the ability to display that current list in the chatroom using !list. It can also tell the user what the current game is, currently it is !test but I may change this later. There are eight other commands that are used that allow the user to send user input to my computer so that I can allow them to interact and play a game (currently Pokemon Gold) using commands: !up, !down, !left, !right, !a, !b, !start, !select.


Instructions: (please go to Readme.MD and click the RAW button on github, it makes the example look better since Github kinda auto-formats things poorly sometimes)

1) Download the files and unzip them.

2a) Make a separate twitch account for your bot 
2b) Get your oauth code from https://twitchapps.com/tmi/ 
2c) Modify the template arguments.txt file to fit your own username and bot rather than the placeholder example


      Modify the arguments.txt file to a 4 lined .txt file containing:
      1) Your Twitch username
      2) Your oauth code generated from "https://twitchapps.com/tmi/"
      3) The current game you are playing
      4) Your Twitch bot's username
      
      Sample arguments.txt file:
      
      TwitchPlaysPokemon
      oauth:123a4bcdefghijklmn5678o9pqrst0
      Twitch Plays
      tpp

3a) Open up cmd on Windows or the terminal on Linux and change the current directory to the folder that you unzipped
3b) compile Config.java and Bot.java

Windows:
javac -cp ".;pircbot.jar" Config.java Bot.java

Linux:
javac -cp ".:pircbot.jar" Config.java Bot.java

3c) run Config.java

Windows:
java -cp .;pircbot.jar Config

Linux:
java -cp .:pircbot.jar Config

If done successfully you should be greeted with something like this:

Bot is now connected to the channel.
12/30/2020 03:51:08: *** Connected to server.
12/30/2020 03:51:08: *** Logged onto server.
12/30/2020 03:51:08: tpp: JOIN
12/30/2020 03:51:08: :tmi.twitch.tv USERSTATE #twitchplayspokemon
12/30/2020 03:51:08: :tmi.twitch.tv ROOMSTATE #twitchplayspokemon

of course except with a different datetime, and your own username and bot rather than the sample's.


4) Once you made sure that's up and running you are good to go! You can edit the java files to do whatever you want to do with them (Bot.java specifically deals with all the commands) with your favorite text editor, but if you change anything then repeat steps 3b) and 3c) for the changes to take effect.
