public class Config
{
   public static String curGame = "";
   public static void main(String [] args) throws Exception
   {
      //You must run three aruments: your twitch username, the oauth key, and the name of the game you are playing
      String user = "#" + args[0];
      String auth = args[1];
      curGame = args[2];
      Bot bot = new Bot();
      bot.setVerbose(true);
      bot.connect("irc.twitch.tv", 6667, auth);
      bot.sendRawLine("CAP REQ :twitch.tv/membership");
      bot.sendRawLine("CAP REQ :twitch.tv/commands");
      bot.joinChannel(user);
      bot.getName();
   }
}