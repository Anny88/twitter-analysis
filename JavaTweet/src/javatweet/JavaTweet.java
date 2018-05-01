/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import twitter4j.GeoLocation;
import twitter4j.Query;
import static twitter4j.Query.POPULAR;
import static twitter4j.Query.RECENT;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class JavaTweet  {

    public static void main(String[] args) throws TwitterException, IOException {  
        ConnectDB con = new ConnectDB();
        FindTweets findTweets = new FindTweets();
        String keyword = "#game";
        int MAX_QUERIES = 1;
        final int TWEETS_PER_QUERY = 10;
        List<String> Baum = new ArrayList();
        Baum.add(keyword.toLowerCase());
               
        try {
            con.drop();
            con.createTable();
            //List <String> tweets = findTweets.findByLoc(keyword, MAX_QUERIES,TWEETS_PER_QUERY);
            findTweets.findByLoc(keyword, MAX_QUERIES,TWEETS_PER_QUERY);
            
           //con.post(tweets);
            con.getRelatedTags(Baum, 15, keyword, MAX_QUERIES*TWEETS_PER_QUERY , null, 0);
            
            /*for(String tweet : tweets) {
                System.out.println(tweet + " : " + NLP.findSentiment(tweet));        
            }*/
         } 
        catch (Exception ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }       
}
    

    
    
