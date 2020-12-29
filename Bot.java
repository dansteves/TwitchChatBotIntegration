import org.jibble.pircbot.*;
import java.util.*;
import java.io.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Bot extends PircBot
{
   static File oldoutput, output, botUserName, oldlog, log, botlists;
   static String botName = "";
   static ArrayList<String> users = new ArrayList<String>();
   static ArrayList<String> bots = new ArrayList<String>();
   static ArrayList<String> chatlines = new ArrayList<String>();
   static int hours = 0;
   static boolean hydration = false;
   static ScheduledExecutorService eS = Executors.newScheduledThreadPool(1);
   static long lastexecution = System.currentTimeMillis();
   static int timethreshold = 3600;
   static String lastadder = "";
   static boolean addcheck = false;
   static int customcounter = 0;
   static String channelowner = "";
   static boolean verboseGuilt = false;
   static boolean isLive = false;
   public Bot() throws FileNotFoundException, IOException
   {
      handleUser();
      handleLog();
      handleBots();
      loadUserName();
      loadLog();
      loadBots();
      this.setName(botName);
      this.isConnected();
      System.out.println("Bot is now connected to the channel.");
   }
   public static void printDistinct(ArrayList<String> users) 
   { 
        String [] completedUsers = users.toArray(new String[users.size()]);
        for (int i = 0; i < completedUsers.length; i++) 
        { 
            int j; 
            for (j = 0; j < i; j++) 
            if (completedUsers[i].equals(completedUsers[j])) 
                break; 
            if (i == j) 
            System.out.println(completedUsers[i]); 
        } 
   }
   public static boolean handleFile() throws IOException
   {
     oldoutput = new File("UserList.txt");
     oldoutput.createNewFile();
     return true;
   }
   public static boolean handleBots() throws IOException
   {
     botlists = new File("botlist.txt");
     botlists.createNewFile();
     return true;
   }
   public static boolean handleLog() throws IOException
   {
     oldlog = new File("ChatLog.txt");
     oldlog.createNewFile();
     return true;
   }
   public static boolean handleUser() throws IOException
   {
     botUserName = new File("arguments.txt");
     botUserName.createNewFile();
     return true;
   }
   public static void saveMenu()
   {
         String [] completedUsers = users.toArray(new String[users.size()]);
         String result = "";
         for(int i = 0; i < completedUsers.length; i++)
         {
            result = result + completedUsers[i].toString();
            result = result + '\n';
         }
         oldoutput = new File("UserList.txt");
         if(oldoutput.canRead() == true)
         {
            oldoutput.delete();
         }
         output = new File("UserList.txt");
         try
         {
            FileWriter fW = new FileWriter(output, false);
            fW.write(result);
            fW.close();
         }
         catch (IOException e) { e.printStackTrace(); }
   }
   public static void saveLog()
   {
         String [] lines = chatlines.toArray(new String[chatlines.size()]);
         String result = "";
         for(int i = 0; i < lines.length; i++)
         {
            result = result + lines[i].toString();
            result = result + '\n';
         }
         oldlog = new File("ChatLog.txt");
         if(oldlog.canRead() == true)
         {
            oldlog.delete();
         }
         log = new File("ChatLog.txt");
         try
         {
            FileWriter fW = new FileWriter(log, false);
            fW.write(result);
            fW.close();
         }
         catch (IOException e) { e.printStackTrace(); }
   }
   public static void loadMenu() throws FileNotFoundException
   {
         Scanner in = new Scanner(oldoutput);
         users = new ArrayList<String>();
         while(in.hasNextLine())
         {
            String nL = in.nextLine();
            users.add(nL);
         }
   }
   public static void loadLog() throws FileNotFoundException
   {
         Scanner in = new Scanner(oldlog);
         chatlines = new ArrayList<String>();
         while(in.hasNextLine())
         {
            String nL = in.nextLine();
            chatlines.add(nL);
         }
   }
   public static void loadBots() throws FileNotFoundException
   {
         Scanner in = new Scanner(botlists);
         bots = new ArrayList<String>();
         while(in.hasNextLine())
         {
            String nL = in.nextLine();
            bots.add(nL);
         }
   }
   public static void loadUserName() throws FileNotFoundException
   {
         Scanner in = new Scanner(botUserName);
         if(in.hasNextLine())
         {
            botName = in.nextLine();
         }
         if(in.hasNextLine())
         {
            botName = in.nextLine();
         }
         if(in.hasNextLine())
         {
            botName = in.nextLine();
         }
         if(in.hasNextLine())
         {
            botName = in.nextLine();
         }
   }
   public void onDisconnect()
   {
      System.out.println("!!");
      while (!isConnected()) 
      {
       try 
       {
            Thread.sleep(5000);
            reconnect();
            sendRawLine("CAP REQ :twitch.tv/membership");
            sendRawLine("CAP REQ :twitch.tv/commands");
            joinChannel(Config.user);
            getName();
       }
       catch (Exception e) 
       {
           // Couldn't reconnect!
           // Pause for a short while...?
       }
      }
   }
   public void log(String line) 
   {
         DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
         Date date = new Date();
         String curTime = dateFormat.format(date);
         if(line.contains(">>>JOIN #"))
         {
            channelowner = "#" +  line.substring(line.indexOf("#") + 1);
         }
         if(!(line.contains("PING :tmi.twitch.tv") || line.contains("PONG :tmi.twitch.tv") || line.contains("tmi.twitch.tv PONG tmi.twitch.tv") || line.contains("PRIVMSG") || line.contains(".tmi.twitch.tv JOIN #") || line.contains(".tmi.twitch.tv PART #") || line.contains(">>>") || line.contains("tmi.twitch.tv 0") || line.contains("tmi.twitch.tv 3") || line.contains("tmi.twitch.tv CAP * ACK")))
         {
         	System.out.println(curTime + ": " + line);
         }
         if(line.contains("PRIVMSG") && line.contains("ACTION"))
         {
            String sender = line;
            sender = sender.substring(sender.indexOf(":") + 1); 
            sender = sender.substring(0, sender.indexOf("!")); 
            sendMessage(channelowner, "/timeout " + sender + " 1 Colored message, automated by {Bot's name}");
         }
         if(line.contains(".tmi.twitch.tv JOIN #") && !(line.contains("streamelements")))
         {
            String person = line;
            person = person.substring(person.indexOf(":") + 1); 
            person = person.substring(0, person.indexOf("!"));
            System.out.println(curTime + ": " + person + ": JOIN");
            if(verboseGuilt == true)
            { 
               sendMessage(channelowner, "/me " + curTime + ": " + person + " - JOIN");
            }
         }
         if(line.contains(".tmi.twitch.tv PART #") && !(line.contains("streamelements")))
         {
            String person = line;
            person = person.substring(person.indexOf(":") + 1); 
            person = person.substring(0, person.indexOf("!"));
            System.out.println(curTime + ": " + person + ": PART");
            if(verboseGuilt == true)
            { 
               sendMessage(channelowner, "/me " + curTime + ": " + person + " - PART");
            } 
         }
         if(line.contains("tmi.twitch.tv HOSTTARGET"))
         {
            isLive = false;
         }
    }
   public void onUnknown(String line)
   {
      if(line.contains("WHISPER") && !(line.contains("streamelements")))
      {
         String user = line;
         user = user.substring(user.indexOf(":") + 1);
         user = user.substring(0, user.indexOf("!"));
         String parsedmessage = line.substring(line.indexOf("WHISPER") + 8 , line.length());
         System.out.println(parsedmessage);
         String message = parsedmessage.substring(parsedmessage.indexOf(":") + 1 , parsedmessage.length());
         System.out.println("<Whisper> : " + user + ": " + message);
         handleGameInput(user, message);
      }
      else
      {
         //System.out.println("LINE: " + line);
      }
   }
   public void onMessage(String channel, String sender, String login, String hostname, String message)
   {
      DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      Date date = new Date();
      String curTime = dateFormat.format(date);
      String timedmessage = "(";
      timedmessage += curTime;
      timedmessage += ") ";
      timedmessage += sender;
      timedmessage += ": ";
      timedmessage += message;
      System.out.println(timedmessage);
      String chanmatch = "#" + sender;
      String chancheck = "#" + lastadder;
      try
      {
            //handleLog();
            //loadLog();
            //chatlines.add(timedmessage);
            //saveLog();
      }
      catch (Exception f){};
      if(message.equalsIgnoreCase("!gamename"))
      {
         sendMessage(channel, "/me The current game is " + Config.curGame);
      }
      if(hydration == false && ((message.equalsIgnoreCase("!hydrate") && channel.equals(chanmatch)) || (message.contains("is now live!") && sender.equals("streamelements"))))
      {
           hydration = true;
           isLive = true;
           eS.scheduleAtFixedRate(new Runnable() 
           {
               
               public void run() 
               {
                  long curTime = System.currentTimeMillis();
                  long diff = curTime - lastexecution;
                  if(hydration == true && (hours == 0 || (diff >= (timethreshold * 99))))
                  {
                     lastexecution = System.currentTimeMillis();
                     int oz = 4*hours;
                     int ml = 120*hours;
                     String hydratemessage = "/me You've been live for just over " + hours;
                     hydratemessage += " hours. By this point in your broadcast you should have consumed at least ";
                     hydratemessage += oz;
                     hydratemessage += "oz (";
                     hydratemessage += ml;
                     hydratemessage += "mL) of water to maintain optimum hydration. ";
                     sendMessage(channel, hydratemessage);
                     hours++;
                  }
               }
           }, 0, timethreshold, TimeUnit.SECONDS);
      }
      if(hydration == true && ((message.equalsIgnoreCase("!dehydrate") && channel.equals(chanmatch)) || (isLive == false)))
      {
         hydration = false;
         hours = 0;  
      }
      if(message.equalsIgnoreCase("!dc") && channel.equals(chanmatch))
      {
         disconnect();
      }
      if(message.equalsIgnoreCase("!end") && channel.equals(chanmatch))
      {
         System.exit(1);
      }
      //Removes over 200 unwanted Twitch Bots from your channel, recommended to use once at some point so that a generous chatter won't accidentally randomly gift a sub to a bot, wasting their money.
      if(message.equalsIgnoreCase("!killbots") && channel.equals(chanmatch))
      {
         String [] bot = bots.toArray(new String[users.size()]);
         for(int i = 0; i < bot.length; i++)
         {
            sendMessage(channel, "/ban " + bot[i]); 
         }
      }
      if(message.equalsIgnoreCase("!verbose") && channel.equals(chanmatch))
      {
         verboseGuilt = !verboseGuilt;
         if(verboseGuilt == true)
         {
            sendMessage(channel, "/me Verbose is set to true.");
         }
         else
         {
            sendMessage(channel, "/me Verbose is set to false.");
         }
      }
      if(message.equalsIgnoreCase("!whereami"))
      {
         sendMessage(channel, "/me I'm currently sleeping (probably), but the game can still progress without me! ' /w (Bot's name) {!u/!d/!l/!r/!a/!b/!start/!select} ' can be used to play!");
      }
      if(message.equalsIgnoreCase("!time"))
      {
         sendMessage(channel, "/me The current time in my time zone (Pacific Time) is: " + curTime);
      }
      if(message.contains("!add"))
      { 
         addcheck = true;
         if(sender.equals("streamelements") == false && sender.equals("nightbot") == false)
         {
            lastadder = sender;
            chancheck = "#" + lastadder;
            System.out.println(lastadder);
         }
         sendMessage(channel, "!followage " + sender);
      }
            if(message.contains("!death") && channel.equals(chanmatch))
            {
               customcounter++;
               sendMessage(channel, "Deaths: " + customcounter);
            }
            if(message.contains("!undo")&& channel.equals(chanmatch))
            {
               customcounter--;
               sendMessage(channel, "Deaths: " + customcounter);
            }
            if(message.contains("Wanna become famous? Buy followers") || message.contains("If you need real free and high quality service to increase your viewers") || message.contains("Was pleasantly surprised haha, content is awsome tho!") || message.contains("In search of followers, primes and views?"))
            {
               sendMessage(channel, "/ban " + sender);
            }
            if(message.toLowerCase().contains("/me"))
            {
               sendMessage(channel, "/timeout " + sender + " 1");
            }
            if(message.contains("!deathset")&& channel.equals(chanmatch))
            {
               int wordCount = 1;
               for (int i = 0; i < message.length(); i++) 
               {
                   if (message.charAt(i) == ' ') 
                   {
                       wordCount++;
                   } 
               }
               if(wordCount == 2)
               {
                  try
                  {
                     customcounter = Integer.parseInt(message.split(" ")[1]); 
                     sendMessage(channel, "Deaths set to: " + customcounter);
                     String line = "Deaths set to: ";
                     line += customcounter;
                     line += "\n";
                     System.out.println(line); 
                     //WriteFile(line); 
                  }    
               catch(NumberFormatException e){}
               }
            }
      if(channel.equals(chancheck) == false && (addcheck == true && (message.contains("is not following") && sender.equals("streamelements"))))
      {
         System.out.println("    addcheck is now false");
         addcheck = false;
         sendMessage(channel, "/timeout " + lastadder + " 600");
         sendMessage(channel, "/me Do you see viewer levels in the title? No. Please don't even bother with add requests if you aren't even followed to the channel at the very least!");
      }
      if(addcheck == true && (channel.equals(chancheck) || ((message.contains("is not following") == false) && sender.equals("streamelements"))))
      {    
         sendMessage(channel, "/me Sorry, nothing.");
      }
      if(message.equalsIgnoreCase("!lvsheet"))
      {
         sendMessage(channel, "/me Sorry, nothing.");
      }
      if(message.equalsIgnoreCase("!twitter"))
      {
         sendMessage(channel, "/me My Twitter profile is https://twitter.com/{xxxxx} !");
      }
      if(message.equalsIgnoreCase("Good!! I am bout to do animated brb, intro and offline screen for your streaming"))
      {
         sendMessage(channel, "/ban " + sender);
      }
      if(message.contains("!bademote"))
      {
         sendMessage(channel, "/me Confused on why your message didn't go through? You probably said one of the two most overused/ugly emotes on twitch. Whatever you meant to say could have probably been better said with a ' Jebaited '.");
      }
      if(message.equalsIgnoreCase("!interest"))
      {
         try
         {
            handleFile();
            loadMenu();
            
            String [] completedUsers = users.toArray(new String[users.size()]);
            if(completedUsers.length == 0)
            {
               users.add(sender);
               sendMessage(channel, sender + " added to list of players interested in me hosting an easy endless tournament"); 
            }
            else
            {
               for (int i = 0; i < completedUsers.length; i++) 
               { 
                  if(completedUsers[i].equals(sender)) 
                  {
                      sendMessage(channel, sender + " is already in the list!");
                      break; 
                  }
                  if(i == completedUsers.length - 1)
                  {
                     users.add(sender);
                     sendMessage(channel, sender + " added to list of players interested in me hosting an easy endless tournament");  
                  }
               } 
            }
                   
            saveMenu();
         }
         catch (Exception f){};
      }     
      if(message.equalsIgnoreCase("!commands"))
      {
         sendMessage(channel, "/me Commands: !{gamename | clearsaturday | bademote | 100EEL | 99Lives | twitter | time | subtember} ");
      }      
      if(message.equalsIgnoreCase("!list"))
      {
         try
         {
            handleFile();
            loadMenu();
            
            String [] completedUsers = users.toArray(new String[users.size()]);
            String list = "Players interested in the easy endless tournament: ";
            int n = completedUsers.length;
            if(n == 0)
            {
               sendMessage(channel, "/me The current list of users interested is empty");   
            }
            else
            {
               for (int i = 0; i < n; i++) 
               { 
                  list += completedUsers[i];
                  if(i < n - 1)
                  {
                     System.out.println(n + " " + i);
                     list += ", "; 
                  }
               }
               sendMessage(channel, list);
            }
            saveMenu();
         }
         catch (Exception f){};
      }
      handleGameInput(sender, message);
   }
   public void handleGameInput(String user, String message)
   {
      boolean validMove = false;
      if(message.equalsIgnoreCase("!up") || message.equalsIgnoreCase("!u"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_8);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_8);
            System.out.println("up");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!down") || message.equalsIgnoreCase("!d"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_2);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_2);
            System.out.println("down");
         }
         catch(Exception g){};  
      }
      if(message.equalsIgnoreCase("!left") || message.equalsIgnoreCase("!l"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_4);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_4);
            System.out.println("left");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!right") || message.equalsIgnoreCase("!r"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_6);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_6);
            System.out.println("right");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!a"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_3);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_3);
            System.out.println("a");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!b"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_0);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_0);
            System.out.println("b");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!select"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_7);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_7);
            System.out.println("select");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!start"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_9);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_9);
            System.out.println("start");
         }
         catch(Exception g){};
      }
   }
}
