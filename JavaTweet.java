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
        final int TWEETS_PER_QUERY = 100;
        List<String> Baum = new ArrayList();
        Baum.add(keyword.toLowerCase());
        TreeNode Baum2 = null;
        TreeNode BaumTree = new TreeNode (keyword.toLowerCase(), 100);
        //BaumTree.parent = null;
               
        try {
            con.drop();
            con.createTable();
            //List <String> tweets = findTweets.findByLoc(keyword, MAX_QUERIES,TWEETS_PER_QUERY);
            findTweets.findByLoc(keyword, MAX_QUERIES,TWEETS_PER_QUERY);
            
           //con.post(tweets);
            Baum2 = con.getRelatedTags(BaumTree, Baum, 15,  MAX_QUERIES*TWEETS_PER_QUERY , keyword, 0);
            System.out.println ("BAUM : \n\n");
            recursivePrint(Baum2);
            /*for(String tweet : tweets) {
                System.out.println(tweet + " : " + NLP.findSentiment(tweet));        
            }*/
         } 
        catch (Exception ex) {
            Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
        }       
        
    }       
    static void recursivePrint(TreeNode node){    
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
}
    

    
    
