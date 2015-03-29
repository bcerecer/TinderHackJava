import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class TinderAPI
{
  private String tinderToken;
  private String tinderID;
  private String facebookToken;
  private String facebookID;
  private String facebookUserName;
  private final ArrayList<TinderUser> recommendedUsers = new ArrayList();
  private final String apiBaseURL = "https://api.gotinder.com/";

  private static final HttpClient client = new DefaultHttpClient();

  public static HttpClient getClient()
  {
    return client;
  }

  public TinderAPI(String facebookToken, String facebookUserName)
  {
    this.facebookUserName = facebookUserName;
    this.facebookToken = facebookToken;
  }

  private void setTinderHeaders(HttpUriRequest post)
  {
    post.setHeader("Content-Type", "application/json");
    post.addHeader("Accept-Language", "tr;q=1");
    post.addHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    post.addHeader("os_version", "700001");
    post.addHeader("Accept", "*/*");
    post.addHeader("platform", "ios");
    post.addHeader("Connection", "keep-alive");
    post.addHeader("app_version", "3");
    post.addHeader("Accept-Encoding", "gzip, deflate");
    post.addHeader("X-Auth-Token", getTinderToken());
  }

  
  private JsonObject getHttpResponse(HttpUriRequest post) throws IOException
  {
    HttpResponse response = getClient().execute(post);
    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuilder resultString = new StringBuilder();
    String line = "";
    while ((line = rd.readLine()) != null) {
      resultString.append(line);
    }
    
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = (JsonObject)parser.parse(resultString.toString());
    return jsonObject;
  }

  public void updateProfile(int distance, int gender, int ageMin, int ageMax, int genderFilter, String bio, boolean discoverable)
    throws UnsupportedEncodingException, IOException
  {
    HttpPost post = new HttpPost("https://api.gotinder.com//profile");
    post.setHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    post.setHeader("Content-Type", "application/json");
    setTinderHeaders(post);
    String veri = new StringBuilder().append("{\n    \"distance_filter\":").append(distance).append(",\n").append("    \"gender\":").append(gender).append(",\n").append("    \"age_filter_min\":").append(ageMin).append(",\n").append("    \"age_filter_max\":").append(ageMax).append(",\n").append("    \"gender_filter\":").append(genderFilter).append(",\n").append("    \"bio\": \"").append(bio).append("\"\n,").append("    \"discoverable\":").append(discoverable).append("\n").append("}").toString();

    post.setEntity(new StringEntity(veri));
    JsonObject json = getHttpResponse(post);
    Logger.getAnonymousLogger().info(new StringBuilder().append("Response: ").append(json).toString());
  }

  public void sendMessage(String tinderID, String message) throws UnsupportedEncodingException, IOException
  {
	 //matchID = herIDmyID
	String matchID = new StringBuilder().append(tinderID).append(getTinderID()).toString();
	  
    HttpPost post = new HttpPost(new StringBuilder().append("https://api.gotinder.com//user/matches/").append(matchID).toString());
    post.setHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    post.setHeader("Content-Type", "application/json");
    setTinderHeaders(post);
    String veri = new StringBuilder().append("{\"message\": \"").append(message).append("\"}").toString();
    post.setEntity(new StringEntity(veri));
    JsonObject json = getHttpResponse(post);
    Logger.getAnonymousLogger().info(new StringBuilder().append("message: ").append(json).toString());
  }
    
  public List getMatches() throws IOException{

	    List<String> allMatches = new LinkedList<String>();
	    
	    HttpPost post = new HttpPost("https://api.gotinder.com/updates");
	    setTinderHeaders(post);
	    String veri = new StringBuilder().append("{\"matches\": \"").append("").append("\"}").toString();
	    post.setEntity(new StringEntity(veri));

	    JsonObject json = getHttpResponse(post);
    	
        JsonArray matches = json.getAsJsonArray("matches");
        
        for (int i = 0; i < matches.size(); i++) 
        {
            JsonObject id = matches.get(i).getAsJsonObject();
            allMatches.add(id.get("participants").getAsString());
        }
                
        return allMatches;
  }
  
  public void messageAll(String message) throws UnsupportedEncodingException, IOException
  {
	    List<String> tinderID = getMatches();
	    
      	for(String str: tinderID)
      	{
    		String matchID = new StringBuilder().append(str).append(getTinderID()).toString();
    	    HttpPost post = new HttpPost(new StringBuilder().append("https://api.gotinder.com//user/matches/").append(matchID).toString());
    	    post.setHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    	    post.setHeader("Content-Type", "application/json");
    	    setTinderHeaders(post);
    	    String veri = new StringBuilder().append("{\"message\": \"").append(message).append("\"}").toString();
    	    post.setEntity(new StringEntity(veri));
    	    JsonObject json = getHttpResponse(post);
    	    Logger.getAnonymousLogger().info(new StringBuilder().append("message: ").append(json).toString());
      	}   
  }
  
  public void updateLocation(double lat, double lng) throws IOException
  {
    HttpPost post = new HttpPost("https://api.gotinder.com//user/ping");
    post.setHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    post.setHeader("Content-Type", "application/json");
    setTinderHeaders(post);
    String veri = new StringBuilder().append("{\"lat\": \"").append(lat).append("\", \"lon\": \"").append(lng).append("\"}").toString();
    post.setEntity(new StringEntity(veri));
    JsonObject json = getHttpResponse(post);
    Logger.getAnonymousLogger().info(new StringBuilder().append("Response: ").append(json).toString());

  }
  
  public void login() throws IOException
  {
    setFacebookID(getFacebookID(getFacebookUserName()));
    HttpPost post = new HttpPost("https://api.gotinder.com/auth");
    post.setHeader("User-Agent", "Tinder/3.0.4(iPhone: iOS 8.3.1: Scale/2.00)");
    post.setHeader("Content-Type", "application/json");
    String veri = new StringBuilder().append("{\"locale\":\"tr\",\"facebook_token\":\"").append(getFacebookToken()).append("\",\"facebook_id\":\"").append(getFacebookID()).append("\"}").toString();
    post.setEntity(new StringEntity(veri));
    JsonObject json = getHttpResponse(post);
    setTinderToken(json.get("token").getAsString());
    JsonObject user = json.getAsJsonObject("user");
    setTinderID(user.get("_id").getAsString());
  }
  
  public void getMyTinderID() throws IOException
  {
	  List<String> allMatches = new LinkedList<String>();
	    
	    HttpPost post = new HttpPost("https://api.gotinder.com/updates");
	    setTinderHeaders(post);
	    String veri = new StringBuilder().append("{\"matches\": \"").append("").append("\"}").toString();
	    post.setEntity(new StringEntity(veri));

	    JsonObject json = getHttpResponse(post);
  	
      JsonArray matches = json.getAsJsonArray("matches");
      
      for (int i = 0; i < matches.size(); i++) 
      {
          JsonObject id = matches.get(i).getAsJsonObject();
          allMatches.add(id.get("participants").getAsString());
      }
  }
  
  
  private String getFacebookID(String userName) throws IOException {
    HttpGet post = new HttpGet(new StringBuilder().append("http://graph.facebook.com/").append(userName).toString());
    JsonObject json = getHttpResponse(post);
    String facebookIDString = json.get("id").getAsString();
    return facebookIDString;
  }
  
  private void testFacebook() throws IOException
  {
      HttpGet post = new HttpGet(new StringBuilder().append("https://graph.facebook.com/oauth/client_secret?client_id=1594896897393966&access_token=bvWQrPd2FEmSqeuw7FJwGNXG1_g&redirect_uri=http://localhost:8080/fbtoken").append("").toString());
      JsonObject json = getHttpResponse(post);
      //String facebookIDString = json.get("id").getAsString();
      
  }

  //holds all the information related to the user
  public TinderUser getUserProfile(String tinderID) throws IOException, ParseException {
    TinderUser tinderUser = new TinderUser("Unknown User");
    HttpGet post = new HttpGet(new StringBuilder().append("https://api.gotinder.com/user/").append(tinderID).toString());
    setTinderHeaders(post);
    JsonObject incomingJson = getHttpResponse(post);
    if (!incomingJson.get("status").toString().equals("\"not found\""))
    {
      JsonObject jsonResult = incomingJson.get("results").getAsJsonObject();

      tinderUser.setName(jsonResult.getAsJsonObject().get("name").getAsString());
      tinderUser.setDistance(jsonResult.getAsJsonObject().get("distance_mi").getAsInt());
      tinderUser.setTinderID(jsonResult.getAsJsonObject().get("_id").getAsString());
      tinderUser.setUserBio(jsonResult.getAsJsonObject().get("bio").getAsString());
      tinderUser.setBirthDate(jsonResult.getAsJsonObject().get("birth_date").getAsString());
      tinderUser.setGender(jsonResult.getAsJsonObject().get("gender").getAsString());
      tinderUser.setUserPictures(jsonResult.getAsJsonObject().get("photos").getAsJsonArray());
      tinderUser.setProfilePictureRL(tinderUser.getUserPictures().get(0).getAsJsonObject().get("processedFiles").getAsJsonArray().get(0).getAsJsonObject().get("url").toString());
    }

    return tinderUser;
  }
  
  //likes all the recommended users and prints them
  public void listRecommendedUsers()
    throws IOException
  {
	  likeThemAll();
  }
  
  //likes all recommended users
  public void likeThemAll() throws IOException
  {
	  
    for (TinderUser tinderUser:this.recommendedUsers)
    {

      boolean amIMatched = likeUser(tinderUser.getTinderID());
      tinderUser.setLikedEachOther(amIMatched);
      Logger.getAnonymousLogger().info(tinderUser.toString());
    }
  }
  
  //like a user given a tinderID. Returns match result as true or false
  public boolean likeUser(String tinderID) throws IOException {
    HttpGet post = new HttpGet(new StringBuilder().append("https://api.gotinder.com/like/").append(tinderID).toString());
        
    setTinderHeaders(post);
    JsonObject json = getHttpResponse(post);

    String matchString = json.get("match").toString();
    return matchString.contains("true");
  }
  
  //parses the json response, gets each recommended user and adds to the list
  public void addRecommendedUsersToMyList(JsonObject jsonResult)
    throws ParseException, IOException
  {
    String jsonString = jsonResult.toString();

    if ((!jsonString.contains("recs timeout")) && (!jsonString.contains("recs exhausted")) && 
      (jsonResult.get("status") != null) && 
      (jsonResult.get("status").toString().contains("200")))
    {
      Logger.getAnonymousLogger().info("Found it");

      JsonArray ar = jsonResult.get("results").getAsJsonArray();

      for (JsonElement col : ar) {
        TinderUser tinderUser = new TinderUser();
        tinderUser.setName(col.getAsJsonObject().get("name").getAsString());
        tinderUser.setDistance(col.getAsJsonObject().get("distance_mi").getAsInt());
        tinderUser.setTinderID(col.getAsJsonObject().get("_id").getAsString());
        tinderUser.setUserBio(col.getAsJsonObject().get("bio").getAsString());
        tinderUser.setBirthDate(col.getAsJsonObject().get("birth_date").getAsString());
        tinderUser.setGender(col.getAsJsonObject().get("gender").getAsString());
        tinderUser.setUserPictures(col.getAsJsonObject().get("photos").getAsJsonArray());
        tinderUser.setAge();
        tinderUser.setProfilePictureRL(tinderUser.getUserPictures().get(0).getAsJsonObject().get("processedFiles").getAsJsonArray().get(0).getAsJsonObject().get("url").toString());
        boolean amIMatched = likeUser(tinderUser.getTinderID());
        System.out.println("This user was liked! + "+ amIMatched);
        tinderUser.setLikedEachOther(amIMatched);
        Logger.getAnonymousLogger().info(tinderUser.toString());
      }
    }
  }
  
  //returns recommended users as JsonObject
  public JsonObject getRecommendedUsers() throws IOException, ParseException
  {
    HttpGet post = new HttpGet("https://api.gotinder.com//user/recs");
    setTinderHeaders(post);
    JsonObject jsonObject = getHttpResponse(post);
    addRecommendedUsersToMyList(jsonObject);
    return jsonObject;
  }
  

 
  public String getTinderToken()
  {
    return this.tinderToken;
  }

  public void setTinderToken(String tinderToken)
  {
    this.tinderToken = tinderToken;
  }
  
  public void setTinderID(String tinderID)
  {
    this.tinderID = tinderID;
  }
  
  public String getTinderID()
  {
    return this.tinderID;
  }

  public String getFacebookToken()
  {
    return this.facebookToken;
  }

  public void setFacebookToken(String facebookToken)
  {
    this.facebookToken = facebookToken;
  }

  public String getFacebookID()
  {
    return this.facebookID;
  }

  public void setFacebookID(String facebookID)
  {
    this.facebookID = facebookID;
  }

  public String getFacebookUserName()
  {
    return this.facebookUserName;
  }

  public void setFacebookUserName(String facebookUserName)
  {
    this.facebookUserName = facebookUserName;
  }
}