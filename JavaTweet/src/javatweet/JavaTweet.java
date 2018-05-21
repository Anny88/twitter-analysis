/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.TwitterException;


public class JavaTweet  {

    public static void main(String[] args) throws TwitterException, IOException {  
        ConnectDB con = new ConnectDB();
        FindTweets findTweets = new FindTweets();
        String keyword = "#usa";
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
            con.getRelatedTags(Baum, 15,  MAX_QUERIES*TWEETS_PER_QUERY , keyword, 0);
            
            /*for(String tweet : tweets) {
                System.out.println(tweet + " : " + NLP.findSentiment(tweet));        
            }*/
         } 
        catch (Exception ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }       
}
    

    
    
