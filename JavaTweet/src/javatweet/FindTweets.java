
package javatweet;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.GeoLocation;
import twitter4j.Query;
import static twitter4j.Query.RECENT;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class FindTweets {
    public static List<Status>  findByLoc() throws TwitterException, IOException, Exception{
        
        Twitter twitter = connectToTwitter();    
            
        double lat = 43.238949;
        double lon = 76.889709;
        double res = 20;
        String resUnit = "mi";   
                  
        Query query = new Query().geoCode(new GeoLocation(lat,lon), res, resUnit); 
        query.count(110); //You can also set the number of tweets to return per page, up to a max of 100
        query.resultType(RECENT);

        QueryResult result = twitter.search(query);
        if(result.hasNext())//there is more pages to load
        {
        query = result.nextQuery();
        result = twitter.search(query);
        }
        writeToCSV(result);       
        return result.getTweets();
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
                    .setOAuthAccessTokenSecret(accessTokenSecret);
            TwitterFactory factory = new TwitterFactory(cb.build());
            Twitter twitter = factory.getInstance();
            return twitter;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    
    public static void writeToCSV (QueryResult result) throws Exception {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter("test.csv"), '\t');
            Boolean includeHeaders = true;

            List<String[]> lines = new ArrayList<>(result.getTweets().size());
            for(Status status : result.getTweets()) {
                lines.add(new String[] { status.getText() });
            }

            writer.writeAll(lines, includeHeaders);
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
