import org.jibble.pircbot.*;

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
   }
}