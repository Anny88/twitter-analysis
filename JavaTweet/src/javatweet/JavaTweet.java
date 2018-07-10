/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javatweet.FindTweets.connectToTwitter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;

import twitter4j.TwitterException;


public class JavaTweet  {

    public static void main(String[] args) throws TwitterException, IOException {  
        ConnectDB con = new ConnectDB();
        FindTweets findTweets = new FindTweets();
        //NLP.init();
        String keyword = "#fun";
        int MAX_QUERIES = 1;
        final int TWEETS_PER_QUERY = 10;
        int Id = 44418; //London WOEID
        double lat = 50;
        double lon = 0;
        
        List<String> Baum = new ArrayList();
        Baum.add(keyword.toLowerCase());
        TreeNode Baum2 = null;
        TreeNode BaumTree = new TreeNode (keyword.toLowerCase(), 100);
        //BaumTree.parent = null;
               
        try {
            con.drop();
            con.createTable();
            
            findTweets.findByLoc(keyword, MAX_QUERIES,TWEETS_PER_QUERY);
            
           
            Baum2 = con.getRelatedTags(BaumTree, Baum, 15,  MAX_QUERIES*TWEETS_PER_QUERY , keyword, 0);
            System.out.println ("BAUM : \n\n");
            
            recursivePrint(Baum2);
           
            System.out.println("\nPlace WOEID: \n");
            int woeid = getWOEID(lat,lon);
            
            if (woeid > 0){
                System.out.println("\n\nTrending topics: \n");
                getHotTopics(23424775);
            }
            
            System.out.println("\n\nSentiments: \n");
            
            showSents(con);
            
            
            /*
            String f = "RT Happy 4th of July to all my USA #friends and #followers! ??? Which American fragrances will you be lighting today in #celebration? ?? #USA #4thofjuly #4th #July #redwhiteandblue #yankeecandle #yankeecandles #red #white #blue #godbless #godblessam… https://t.co/Afa8tr8py7 https://t.co/6LMSJCB7ot";
            
            String h = "This movie doesn't care about cleverness or humor";
            String r = "Rodger Dodger is one of the most compelling variations on this theme";
            String b = "What is happening right now to children and families in USA in Torture: Stop separating children from their parents at the border,  FamiliesBelongTogether";
            String str = "99.9% Of US Politicians Are Actual Psychopaths, New Study Reveals  https://t.co/qMIjiwM0Oh #NWO @realDonaldTrump #POTUS #Anonymous #USA";
            NLP.init();
            int a = NLP.findSentiment(f);
            System.out.println(a);
            
            //List<String> list = FindTweets.getWords("@B#aggag a#kjdk@ #ij,iji#jij, i # #a, #@ #doy");
            //System.out.print(list);
           
            
            /*for(String tweet : tweets) {
                System.out.println(tweet + " : " + NLP.findSentiment(tweet));        
            }*/
         } 
        catch (Exception ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }    
    public static void getHotTopics(int id) {
        try {
            Twitter twitter = connectToTwitter();
            Trends trends = twitter.getPlaceTrends(id);
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < 10) {
                    System.out.println(trend.getName());
                    count++;
                }
            }       
        } 
        catch (TwitterException ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int getWOEID(double Latitude, double Longitude){
        
        try {
            String url = "https://query.yahooapis.com/v1/public/yql?q=select%20woeid%20from%20geo.places%20where%20text%3D%22(" + Latitude + "," + Longitude + ")%22%20limit%201&diagnostics=false";
            
            Document yahooApiResponse = Jsoup.connect(url).timeout(10 * 1000).get();
            String xmlString = yahooApiResponse.html();
            Document doc = Jsoup.parse(xmlString, "", Parser.xmlParser());
            
            System.out.println(doc.select("woeid").first().text());
            return Integer.parseInt(doc.select("woeid").first().text().toString());
        } 
        catch (IOException ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    
    
    public static void recursivePrint(TreeNode node){    
        System.out.print (node.getTag()+ " "+ node.getValue()+ "  ");
        List<TreeNode> children = node.getChildren(); 
        //if (node.parent.getTag() != null){
        System.out.println ("parent: " + node.getParent().getTag());
        //}
        TreeNode result = null;
        
        for (int i = 0; result == null && i < children.size(); i++) {         
            recursivePrint(children.get(i));
        }
    }
    public  static void showSents (ConnectDB con){
        ResultSet sents = con.selectSentiments();
        int sum = 0;
        int count[] = {0,0,0,0,0};
        int c = 0;
        float av = 0;
        try {
            while (sents.next()){
                int score = sents.getInt(1);
                if (score >= 0 && score <5){
                    sum+=score;
                    c++;
                    count[score]++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }
        av = (float)sum/c;
        System.out.println("sum: " + sum + " count: " + c);
        for (int i=0; i< 5; i++){
            System.out.println("Sentiment " + i + ": " + (float)count[i]/c*100 + "%");
        }
        System.out.println("\nAverage: " + av);
        
    }
}
    

    
    
