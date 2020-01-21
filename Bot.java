import org.jibble.pircbot.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Bot extends PircBot
{
   public Bot()
   {
      this.setName("DStevesBot");
      this.isConnected();
      System.out.println("connected!!!!");
   }
   public void onMessage(String channel, String sender, String login, String hostname, String message)
   {
      System.out.printf("Scanning message from channel %s, sender %s, login %s, hostname %s, message %s. \n", channel, sender, login, hostname, message);
      if(message.equalsIgnoreCase("!test"))
      {
         sendMessage(channel, "The current game is " + Config.curGame);
      }
      if(message.equalsIgnoreCase("!up"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_9);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_9);
            System.out.println("up");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!down"))
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
      if(message.equalsIgnoreCase("!left"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_3);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_3);
            System.out.println("left");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!right"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_4);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_4);
            System.out.println("right");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!a"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_5);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_5);
            System.out.println("a");
         }
         catch(Exception g){};
      }
      if(message.equalsIgnoreCase("!b"))
      {
         try
         {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_6);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_6);
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
            robot.keyPress(KeyEvent.VK_8);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_8);
            System.out.println("start");
         }
         catch(Exception g){};
      }
   }
}