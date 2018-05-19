package javatweet;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import twitter4j.GeoLocation;
import twitter4j.Query;
import static twitter4j.Query.RECENT;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FindTweets {
    
    public static List<String>  findByLoc(String keyword, int MAX_QUERIES, int TWEETS_PER_QUERY) throws TwitterException, IOException, Exception{
        
        Twitter twitter = connectToTwitter();    
        
        ConnectDB conDB = new ConnectDB();
        Connection con = conDB.getConnection();
        
        double lat = 51.509865;
        double lon = -0.118092;
        double res = 30;
        String resUnit = "mi";  
        List<String> lines = new ArrayList<>();
        
        //final int MAX_QUERIES = 4;
        //final int TWEETS_PER_QUERY = 100;
        int	totalTweets = 0;
        long maxID = -1;
        Map<String, RateLimitStatus> rateLimitStatus = twitter.getRateLimitStatus("search");
        RateLimitStatus searchTweetsRateLimit = rateLimitStatus.get("/search/tweets");
              
        System.out.printf("You have %d calls remaining out of %d, Limit resets in %d seconds\n",
            searchTweetsRateLimit.getRemaining(),
            searchTweetsRateLimit.getLimit(),
            searchTweetsRateLimit.getSecondsUntilReset());
        
        try{
        
            for (int queryNumber = 0; queryNumber < MAX_QUERIES; queryNumber++) {
                System.out.printf("\n! Starting loop %d\n", queryNumber);
                if (searchTweetsRateLimit.getRemaining() == 0) {
                    System.out.printf("!!! Sleeping for %d seconds due to rate limits\n", searchTweetsRateLimit.getSecondsUntilReset());
                    Thread.sleep((searchTweetsRateLimit.getSecondsUntilReset()+2) * 1000l);
                }

                Query query = new Query(keyword).geoCode(new GeoLocation(lat,lon), res, resUnit); 
                query.count(TWEETS_PER_QUERY);
                query.resultType(RECENT);
                query.setLang("en");

                QueryResult result = twitter.search(query);

                if (maxID != -1) {
                    query.setMaxId(maxID - 1);
                }         

                if (result.getTweets().size() == 0) {
                    break; // Nothing? We must be done
                }

                for (Status s: result.getTweets()) {
                    totalTweets++;
                    String tweetReady = null;
                    if (maxID == -1 || s.getId() < maxID) {
                        maxID = s.getId();
                    }
                    if (!s.isRetweet()){
                       tweetReady = cleanText(s.getText());
                    }
                   
                    if (s.isRetweet()){
                       tweetReady = "RT " + cleanText(s.getRetweetedStatus().getText()); 
                    }
                    PreparedStatement posted = con.prepareStatement(insert(tweetReady));
                    posted.executeUpdate();
                    
                    lines.add( cleanText(s.getText()));
                    //lines.add(s.getText());
                }
                searchTweetsRateLimit = result.getRateLimitStatus();
            }
        }
        catch (Exception e) {
            System.out.println("That didn't work well...wonder why?");
            e.printStackTrace();
        }

        System.out.printf("\n\nA total of %d tweets retrieved\n", totalTweets);
        return lines;
      
    }
    
    public static String insert(String tweetReady) {
       int score = 0;
       String insert = null, values = null;
       int size = 0;
        try{
            List <String> tags = getWords(tweetReady);
            insert = "INSERT INTO tweets(maintweet,score";
            values = " VALUES ('"+tweetReady+"', '"+score+"'";
            if (tags.size() > 15) {
                size = 15;
            }
            else { 
                size = tags.size();
            }
            for (int j = 0; j<size; j++){
               String num = Integer.toString(j+1);
               insert += ", tag" + num;
               values += ", '"+tags.get(j)+"' ";
            }
            insert += ")";
            values += ")";
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(tweetReady);
        }
        return (insert+values);
    } 
    
    
  
    
    public static List getWords(String tweet) throws Exception{
        String tweet2 =  tweet.replaceAll("[^a-zA-Z0-9#@_]+"," ");
        String[] subStr;
        List <String> tags = new ArrayList();
        String delimeter = " "; // Разделитель
        if (tweet2.length() > 0){
            try{
                subStr = tweet2.split(delimeter); 
                for(int i = 0; i < subStr.length; i++) { 
                    char first = subStr[i].charAt(0);
                    if (first == '#'){
                        tags.add(subStr[i]);
                    }
                }
            }
            catch (Exception e){
               System.out.println("Error in getWords" + e);  
            }
        }
        if (tags.isEmpty()) {
            //tags.add("notag");
        }
        return tags;
    }
    
    
    
     public static String cleanText(String text) {
        //text = text.replace("\n", "\\n");
        //text = text.replace("\t", "\\t");
         text = text.replace("'", "\\'");
        //text = text.replaceAll("[^a-zA-Z0-9#@]+","");
        return text;
     }
    
     
    public static Twitter connectToTwitter() throws TwitterException{
        try {
            String consumerKey = "ku7BH6QZD1pUq3iQ4ZDzE0dXw";
            String consumerSecret = "3QYliH3QJ5pKhmjcAHsWSRjhzStfX3dDu5vSNuAm85Po1aGdnp";
            String accessToken = "970942326284615680-tiLuPn73gBUkAtXEYokoPFDGv0AOFow";
            String accessTokenSecret = "mVqDgh7u6MzxI11pyHf8gC6FjMBqJsfj4rA1EJAxXIpFs";
                    
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(consumerKey)
                    .setOAuthConsumerSecret(consumerSecret)
                    .setOAuthAccessToken(accessToken)
                    .setTweetModeExtended(true)
                    .setOAuthAccessTokenSecret(accessTokenSecret);
                    //.setTweetModeExtended(true);
            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();
            return twitter;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    public static void writeToCSV ( List <String> lines) throws Exception {
        try {
            //CSVWriter writer = new CSVWriter(new FileWriter("test.csv"), '\t');
            
            FileWriter writer = new FileWriter("test.csv");
            String collect = lines.stream().collect(Collectors.joining(","));
            writer.write(collect);
            //CSVUtils.writeLine(writer, lines);
            writer.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    
    public static void updateSt(Twitter twitter){
        try {
            Status status;
            status = twitter.updateStatus("hh");
            System.out.println("status updated");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
