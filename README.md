# TwitchChatBotIntegration
Allows users to read and write text from/to IRC Twitch chat as a substitute for user input for some of my previous existing projects. Config.java contains the main method of the program and you must run three arguments: your twitch username, your oauth key, and the name of the game you are playing. Bot.java is where all of my existing commands are. Currently Bot.java at the moment is set up to add users to a text file that is loaded and saved each time the command is run if they are interested in entering a tournament I would like to host using the command !interest, and has the ability to display that current list in the chatroom using !list. It can also tell the user what the current game is, currently it is !test but I may change this later. There are eight other commands that are used that allow the user to send user input to my computer so that I can allow them to interact and play a game (currently Pokemon Gold) using commands: !up, !down, !left, !right, !a, !b, !start, !select.


Instructions: 
1) Make sure you have an updated JDK, to do that, make sure you visit https://www.oracle.com/java/technologies/javase-downloads.html and download the latest JDK.

2) Download the files from my repository, by hitting the green button that says code on it, and unzip them.

3a) Make a separate twitch account for your bot\
3b) Get your oauth code from https://twitchapps.com/tmi/ \
3c) Modify the template arguments.txt file to fit your own username and bot rather than the placeholder example: 


       Modify the arguments.txt file to a 4 lined .txt file containing:
       1) Your Twitch username
       2) Your oauth code generated from "https://twitchapps.com/tmi/"
       3) The current game you are playing
       4) Your Twitch bot's username

       Sample arguments.txt file: 
       (PLEASE MAKE SURE YOU UPDATE AND SAVE THIS FILE OR YOU WILL NOT CONNECT TO THE CORRECT CHANNEL)

       TwitchPlaysPokemon
       oauth:123a4bcdefghijklmn5678o9pqrst0
       Twitch Plays
       tpp

4a) Open up cmd on Windows or the terminal on Linux and change the current directory to the folder that you unzipped \
4b) compile Config.java and Bot.java:

       Windows:
       javac -cp ".;pircbot.jar" Config.java Bot.java
       
       Linux:
       javac -cp ".:pircbot.jar" Config.java Bot.java

4c) run Config.java:

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


5) Once you made sure that's up and running you are good to go! You can edit the java files to do whatever you want to do with them (Bot.java specifically deals with all the commands) with your favorite text editor, but if you change anything then repeat steps 4b) and 4c) for the changes to take effect. Don't forget to give your bot moderator privileges in your channel "/mod <botname>" for full effect.

6) I use this bot in tandem with StreamElement's for all of the verified bot's privileges that you can't get with this unverified bot, so optionally you can go and get StreamElement's bot as well. https://streamelements.com/ . Their bot gives you less freedom with what you can do like you cannot customize StreamElements to affect your keyboard like you can with this bot for example, but can still do special things integrated into Twitch's database itself such as look up someone's followage to your channel or how long they have had a channel for, for example. That's why it's nice to have this custom bot and a well established bot together.
