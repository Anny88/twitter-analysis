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
import twitter4j.GeoLocation;
import twitter4j.Query;
import static twitter4j.Query.POPULAR;
import static twitter4j.Query.RECENT;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class JavaTweet  {

    public static void main(String[] args) throws TwitterException, IOException {  
        ConnectDB con = new ConnectDB();
        FindTweets findTweets = new FindTweets();
                       
        try {
            con.drop();
            con.createTable();
            List <Status> tweets = findTweets.findByLoc();
            for (Status s: tweets){
                String tweet = s.getText();
                List tags = getWords(tweet);
                con.post(tweet,tags);
                
                System.out.println(s);
                
            }   
            
        } 
        catch (Exception ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }
           
    public static List getWords(String tweet) throws Exception{
        //String tweet = "Мой результат в мини-игре Пляжные фишки в Paradise Island 2: 156 #GameInsight #ParadiseIsland2";
        String[] subStr;
        //String[] tags = new String[100];
        List <String> tags = new ArrayList();
        //tags.set(0, "notag");
        //int j = 0;
        String delimeter = " "; // Разделитель
        
        try{
            subStr = tweet.split(delimeter); // Разделения строки str с помощью метода split()
            // Вывод результата на экран
            for(int i = 0; i < subStr.length; i++) { 
                //System.out.println(subStr[i]); 
                char first = subStr[i].charAt(0);

                if (first == '#'){
                    //tags[j] = subStr[i];
                   // j++;
                    tags.add(subStr[i]);
                }
            }

            //System.out.println("tags: \n"); 
            //System.out.println(tags); 
        }
        catch (Exception e){
           System.out.println(e);  
        }
        
        if (tags.isEmpty()) {
            tags.add("notag");
        }
        return tags;
    }       
       
}
    

    
    
