import com.google.gson.JsonArray;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TinderUser
{
  private int distance;
  private int commonLikesCount;
  private int commonFriendsCount;
  private String[] commonLikes;
  private String[] commonFriends;
  private String tinderID;
  private String userBio;
  private String birthDate;
  private String gender;
  private String name;
  private int age;
  private JsonArray userPictures;
  private String profilePictureRL;
  private String match;
  private String userMatches;
  private boolean newUser = false;
  private boolean likedEachOther = false;
  private StringBuilder userString = new StringBuilder();

  public TinderUser() {
  }
  public TinderUser(String name) {
    this.name = name;
  }

  public int getDistance()
  {
    return this.distance;
  }

  public void setDistance(int distance)
  {
    distance = (int)Math.round(distance * 1.6D);
    this.distance = distance;
  }

  public int getCommonLikesCount()
  {
    return this.commonLikesCount;
  }

  public void setCommonLikesCount(int commonLikesCount)
  {
    this.commonLikesCount = commonLikesCount;
  }

  public int getCommonFriendsCount()
  {
    return this.commonFriendsCount;
  }

  public void setCommonFriendsCount(int commonFriendsCount)
  {
    this.commonFriendsCount = commonFriendsCount;
  }

  public String[] getCommonLikes()
  {
    return this.commonLikes;
  }

  public void setCommonLikes(String[] commonLikes)
  {
    this.commonLikes = commonLikes;
  }

  public String[] getCommonFriends()
  {
    return this.commonFriends;
  }

  public void setCommonFriends(String[] commonFriends)
  {
    this.commonFriends = commonFriends;
  }

  public String getTinderID()
  {
    return this.tinderID;
  }

  public void setTinderID(String tinderID)
  {
    this.tinderID = tinderID;
  }

  public String getUserBio()
  {
    return this.userBio;
  }

  public void setUserBio(String userBio)
  {
    this.userBio = userBio;
  }

  public String getBirthDate()
  {
    return this.birthDate;
  }

  public void setBirthDate(String birthDate)
  {
    this.birthDate = birthDate;
  }

  public String getGender()
  {
    return this.gender;
  }

  public void setGender(String gender)
  {
    if (gender.equals("1"))
      this.gender = "Female";
    else
      this.gender = "Male";
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public JsonArray getUserPictures()
  {
    return this.userPictures;
  }

  public void setUserPictures(JsonArray userPictures)
  {
    this.userPictures = userPictures;
  }

  private Date stringToDate(String birthString) throws ParseException {
    birthString = birthString.substring(0, birthString.indexOf("T"));
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date date = format.parse(birthString);
    return date;
  }

  public int calculateAge(Date date)
  {
    Calendar calendar = Calendar.getInstance();

    Calendar calendarnow = Calendar.getInstance();

    calendarnow.getTimeZone();

    calendar.setTime(date);

    int getmonth = calendar.get(2);

    int getyears = calendar.get(1);

    int currentmonth = calendarnow.get(2);

    int currentyear = calendarnow.get(1);

    int age = (currentyear * 12 + currentmonth - (getyears * 12 + getmonth)) / 12;

    return age;
  }

  public String toString()
  {
    try
    {
      String matchString = "No";
      if (isLikedEachOther())
        matchString = "Yes";
      this.userString.append("\n--------------------------------\n");

      this.userString.append("Name:").append(getName()).append("\nGender:").append(getGender()).append("\nAge:").append(getAge()).append("\nTinder ID:").append(getTinderID()).append("\nDistance:").append(getDistance()).append("KM\nDid We Like Each Other?:").append(matchString).append("\nUser Bio:").append(getUserBio());
      this.userString.append("\nProfile Picture:").append(getProfilePictureRL());
      this.userString.append("\n-------------------------------\n");
    }
    catch (ParseException ex)
    {
      Logger.getLogger(TinderUser.class.getName()).log(Level.SEVERE, null, ex);
    }
    return this.userString.toString();
  }

  public boolean isNewUser()
  {
    return this.newUser;
  }

  public void setNewUser(boolean newUser)
  {
    this.newUser = newUser;
  }

  public boolean isLikedEachOther()
  {
    return this.likedEachOther;
  }

  public void setLikedEachOther(boolean likedEachOther)
  {
    this.likedEachOther = likedEachOther;
  }

  public int getAge()
    throws ParseException
  {
    return calculateAge(stringToDate(getBirthDate()));
  }

  public void setAge()
    throws ParseException
  {
    this.age = calculateAge(stringToDate(getBirthDate()));
  }

  public String getProfilePictureRL()
  {
    return this.profilePictureRL;
  }

  public void setProfilePictureRL(String profilePictureRL)
  {
    this.profilePictureRL = profilePictureRL;
  }
  
  public String getMatch()
  {
	  return this.match;
  }




}