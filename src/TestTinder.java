import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Timer;

import org.jsoup.Connection.Base;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class TestTinder
{
  public static void main(String[] args)
    throws IOException, ParseException
  {
	//args[0] = facebookToken, args[1]=facebookID
	TinderAPI tinder = new TinderAPI(args[0],args[1]);
    tinder.login();
    
    tinder.messageAll("Hello how you doing? :D" );
    
    
/*
    Timer tinderTimer = new Timer();
    TinderTask tinderTask = new TinderTask(tinder);
    tinderTimer.schedule(tinderTask, 0L, 3000L);
    */
    
  }
}