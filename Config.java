import java.util.*;
import java.io.*;
public class Config
{
   public static File input;
   public static String user = "";
   public static String auth = "";
   public static String curGame = "";
   public static void main(String [] args) throws Exception
   {
      /*
      
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
      
      */
      
      handleFile();
      loadargs();
      Bot bot = new Bot();
      bot.setVerbose(true);
      bot.connect("irc.twitch.tv", 6667, auth);
      bot.sendRawLine("CAP REQ :twitch.tv/membership");
      bot.sendRawLine("CAP REQ :twitch.tv/commands");
      bot.joinChannel(user);
      bot.getName();
   }
   public static boolean handleFile() throws IOException
   {
     input = new File("arguments.txt");
     input.createNewFile();
     return true;
   }
   public static void loadargs() throws FileNotFoundException
   {
         Scanner in = new Scanner(input);
         if(in.hasNextLine())
         {
            user = "#" + in.nextLine();
         }
         if(in.hasNextLine())
         {
            auth = in.nextLine();
         }
         if(in.hasNextLine())
         {
            curGame = in.nextLine();
         }
   }
}
