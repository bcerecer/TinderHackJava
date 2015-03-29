import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TinderTask extends TimerTask
{
  private final TinderAPI tinder;

  public TinderTask(TinderAPI tinderAPI)
  {
    this.tinder = tinderAPI;

  }

  public void run()
  { 
    try {
      this.tinder.addRecommendedUsersToMyList(this.tinder.getRecommendedUsers());
    }
    catch (Exception ex)
    {
      Logger.getLogger(TinderTask.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}