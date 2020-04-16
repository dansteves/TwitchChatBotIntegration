import org.jibble.pircbot.*;
import java.util.*;
import java.io.*;
//import PircBot.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot extends PircBot
{
   static File oldoutput, output, botUserName;
   static String botName = "";
   static ArrayList<String> users = new ArrayList<String>();
   static int hours = 0;
   static boolean hydration = false;
   static ScheduledExecutorService eS = Executors.newScheduledThreadPool(1);
   static long lastexecution = System.currentTimeMillis();
   static int timethreshold = 3600;
   static String lastadder = "";
   static boolean addcheck = false;
   public Bot() throws FileNotFoundException, IOException
   {
      //Make sure you have a file named MyBot.txt that only has the UserName you set for the bot.
      handleUser();
      loadUserName();
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
   public static boolean handleUser() throws IOException
   {
     botUserName = new File("MyBot.txt");
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
   public static void loadUserName() throws FileNotFoundException
   {
         Scanner in = new Scanner(botUserName);
         if(in.hasNextLine())
         {
            botName = in.nextLine();
         }
   }
   public void onMessage(String channel, String sender, String login, String hostname, String message)
   {
      System.out.printf("Scanning message from channel %s, sender %s, login %s, hostname %s, message: %s    . \n", channel, sender, login, hostname, message);
      String chanmatch = "#" + sender;
      String chancheck = "#" + lastadder;
      if(message.equalsIgnoreCase("!gamename"))
      {
         sendMessage(channel, "The current game is " + Config.curGame);
      }
      if(hydration == false && ((message.equalsIgnoreCase("!hydrate") && channel.equals(chanmatch)) || (message.contains("is now live!") && sender.equals("streamelements"))))
      {
         //System.out.println("Hydrating.");
         //sendMessage(channel, "Yes, master. ");
         //ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
         //eS.scheduleAtFixedRate(Bot::runTimer, 0 , 5, TimeUnit.SECONDS);
           hydration = true;
           eS.scheduleAtFixedRate(new Runnable() 
           {
               
               public void run() 
               {
                  long curTime = System.currentTimeMillis();
                  long diff = curTime - lastexecution;
                  //System.out.println(diff);
                  if(hydration == true && (hours == 0 || (diff >= (timethreshold * 99))))
                  {
                     lastexecution = System.currentTimeMillis();
                     int oz = 4*hours;
                     int ml = 120*hours;
                     String hydratemessage = "You've been live for just over " + hours;
                     hydratemessage += " hours. By this point in your broadcast you should have consumed at least ";
                     hydratemessage += oz;
                     hydratemessage += "oz (";
                     hydratemessage += ml;
                     hydratemessage += "mL) of water to maintain optimum hydration. ";
                     //hydratemessage += new java.util.Date();
                     sendMessage(channel, hydratemessage);
                     hours++;
                  }
               }
           }, 0, timethreshold, TimeUnit.SECONDS);
      }
      if(hydration == true && (message.equalsIgnoreCase("!dehydrate") && channel.equals(chanmatch)))
      {
         hydration = false;
         hours = 0;  
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
      if(channel.equals(chancheck) == false && (addcheck == true && (message.contains("is not following") && sender.equals("streamelements"))))
      {
         System.out.println("    addcheck is now false");
         addcheck = false;
         sendMessage(channel, "/timeout " + lastadder + " 600");
         sendMessage(channel, "Do you see viewer levels in the title? No. Please don't even bother with add requests if you aren't even followed to the channel at the very least!");
      }
      if(addcheck == true && (channel.equals(chancheck) || ((message.contains("is not following") == false) && sender.equals("streamelements"))))
      {
         addcheck = false;
         sendMessage(channel, "!lv");
         sendMessage(channel, "!lvsheet");
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
               sendMessage(channel, "The current list of users interested is empty");   
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