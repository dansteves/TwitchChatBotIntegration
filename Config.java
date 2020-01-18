public class Config
{
   public static String curGame = "MM2";
   public static void main(String [] args) throws Exception
   {
      Bot bot = new Bot();
      bot.setVerbose(true);
      bot.connect("irc.twitch.tv", 6667, "oauth:jfo3kvk5s471hy0n07axsjc240e53y");
      bot.joinChannel("#dsteves");
      bot.getName();
   }
}