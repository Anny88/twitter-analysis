package javatweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anna
 */
public class ConnectDB {
     public static Connection getConnection() throws Exception {
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3307/twitter";
            String username = "root";
            String password = "SleepyTiger7744@";
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            System.out.println("connected");
            return conn;
        }
        catch (Exception e){
            System.out.println(e);
            
        }
        return null;
    }
     
     public static void createTable() throws Exception {
         try{
            Connection con = getConnection();
            String createTable = "CREATE TABLE IF NOT EXISTS tweets("
                     + "id int NOT NULL AUTO_INCREMENT,"
                     + "maintweet VARCHAR(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL, "
                     + "score int,"
                     + "tag1 VARCHAR(280), "
                     + "tag2 VARCHAR(280),"
                     + "tag3 VARCHAR(280),"
                     + "tag4 VARCHAR(280),"
                     + "tag5 VARCHAR(280),"
                     + "tag6 VARCHAR(280),"
                     + "tag7 VARCHAR(280),"
                     + "tag8 VARCHAR(280),"
                     + "tag9 VARCHAR(280),"
                     + "tag10 VARCHAR(280),"
                     + "tag11 VARCHAR(280),"
                     + "tag12 VARCHAR(280),"
                     + "tag13 VARCHAR(280),"
                     + "tag14 VARCHAR(280),"
                     + "tag15 VARCHAR(280),"
                     + "PRIMARY KEY (id) )";
            String alterTable = "ALTER TABLE tweets CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_bin"; 
            PreparedStatement create = con.prepareStatement(createTable);
            create.executeUpdate();
            PreparedStatement alter = con.prepareStatement(alterTable);
            alter.executeUpdate();
         }
         catch (Exception e){
             System.out.println(e);
         }
         finally {System.out.println("Create table complete");}
     }
     
    
    
    
   /* public static void post(List <String> tweets) throws Exception{
         Connection con = getConnection();
         //NLP.init();
         for (int i=0; i<tweets.size(); i++){
            String main = tweets.get(i);
            List <String> tags = getWords(main);
            //int score = NLP.findSentiment(main);
            int score = 0;
            try{
                String insert = "INSERT INTO tweets(maintweet,score";
                String values = " VALUES ('"+main+"', '"+score+"'";
                for (int j = 0; j<tags.size(); j++){
                   String num = Integer.toString(j+1);
                   insert += ", tag" + num;
                   values += ", '"+tags.get(j)+"' ";
                }

                insert += ")";
                values += ")";

                PreparedStatement posted = con.prepareStatement(insert+values);
                posted.executeUpdate();
            }
            catch (Exception e){
                System.out.println(e);
                System.out.println(main);
            }
         }
         //finally {System.out.println("Insert complete");}
         
     }
    */
    
    
     public static void drop() throws Exception{
         try{
             Connection con = getConnection();
             PreparedStatement drop = con.prepareStatement("DROP table tweets");
             drop.executeUpdate();
         }
         catch (Exception e){
             System.out.println(e);
         }
         finally {System.out.println("Drop complete");}
     }
     
    
    
     
     public static TreeNode getRelatedTags(TreeNode baum, int numberOfTags,  
                                            String tagToSearch, boolean analyseSents, int layer){
        System.out.println("Layer " + Integer.toString(layer));
        System.out.println("We search " + tagToSearch+ "\n");
        
        JavaTweet jt = new JavaTweet();
        JavaTweet.recursivePrint(baum);
        //System.out.println(tree);
        
        String[] popTags = new String[4];
        int[] popValues = new int[4];
        int count = 0;
        HashMap <String, Integer> tags = new HashMap<String, Integer>();
        ResultSet rs = null;
        int [] Scores = {0,0,0,0,0,0,0};
        tagToSearch = tagToSearch.toLowerCase();        
        
        try{
            rs = selectTags(numberOfTags, tagToSearch);
            
            System.out.println("Tags : ");
            while (rs.next()) {
                count++;
                for (int j = 1; j <= numberOfTags; j++){
                    String tag = (rs.getString(j));
                    
                    if (tag!= null){
                        
                        String tagLC = tag.toLowerCase();
                        //if (layer == 1) {System.out.println(tagLC);}
                        if (recursiveSearch(tagLC, baum)== null){
                        //if (!tree.contains(tagLC)) {
                        //if ((tag != null) && (!tag.toLowerCase().equals(key.toLowerCase()))){
                            if (tags.get(tagLC) == null){
                                tags.put(tagLC, 1);
                                //System.out.println(tag);
                            } 
                            else {
                                Integer t = (Integer) tags.get(tagLC) + 1;
                                tags.put(tagLC, t);
                                //System.out.println(tag);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            System.out.println("Exception in resultset: " + e);
        }
        System.out.println("Count : " + count + "\n");        
        
        
               
        
        try{   
            for (int k = 0; k <4; k++){
                Iterator<Map.Entry<String, Integer>> itr = tags.entrySet().iterator();
                int maxValueInMap=(Collections.max(tags.values())); 
                while(itr.hasNext()){
                    Map.Entry<String, Integer> entry = itr.next();
                    if (entry.getValue()==maxValueInMap) {
                        popTags[k] = entry.getKey();
                        popValues[k] =  entry.getValue();
                        //tree.add(entry.getKey());
                        
                        //recursiveSearch (tagToSearch, baum).addChild(popTags[k], popValues[k]*100/count);
                        itr.remove();
                        break;
                    } 
                }
            tags.remove(popTags[k]);
            System.out.println("Tag " + (k+1) + ": " + popTags[k] + " with value " + (popValues[k]*100)/count+"%");
            
            if (analyseSents){
                try{
                    Scores = selectSentScore(numberOfTags, popTags[k], false);
                }
                catch (Exception e){
                    System.out.println("Exception in Sentiment Scores Select: " + e);
                }
            }
            //add a new Node with Hashtag, Value and Sentiment Score to the Tree
            recursiveSearch (tagToSearch, baum).addChild(popTags[k], popValues[k]*100/count, Scores);
            }
            
        } catch (Exception e){
            System.out.println("Exception in map: " + e);
        }
        layer++;
        
        try{
            if (layer <3){
                for (int m = 0; m <4 ; m++){
                    getRelatedTags(baum, numberOfTags, popTags[m], analyseSents, layer);
                }    
            }
        }
         catch (Exception e){
             System.out.println("Exception in recursion: " + e);
        } 
        return baum;
    }
     
    
    public static ResultSet selectSentiments(){
        ResultSet rs = null;
        String selectScore = "select score from tweets";
                
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(selectScore);            
            return rs;
            //stmt.close();
           
        } catch (Exception e){
            System.out.println("Exception in select " + e);
        }
        return rs;
         
    } 
     
    public static ResultSet selectTags(int numberOfTags, String tagToSearch){
        ResultSet rs = null;
        String selectTags = "select tag1";
                 
        for (int i=1; i<numberOfTags; i++){
            String num = Integer.toString(i+1);
            selectTags += ", tag" + num;
        }
        selectTags+= " from tweets"; 
        
        if (tagToSearch != null){
            selectTags+= " where tag1 = '"+tagToSearch+"' ";
            for (int i=1; i<numberOfTags; i++){
                String num = Integer.toString(i+1);
                selectTags += " OR tag" + num + " = '"+tagToSearch+"'";
            }
        }
        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(selectTags);
            
            // stmt.close();
             return rs;
           
        } catch (Exception e){
            System.out.println("Exception in select " + e);
        }
        return rs;
         
    }
     
    public static int[] selectSentScore (int numberOfTags, String tagToSearch, boolean isRoot){
        int[] result = {0,0,0,0,0,0,0};
        result[0] = 0;
        ResultSet rs = null;
        String selectTags = "";     
             
        if (tagToSearch != null){
            if (isRoot){
                selectTags = "select score from tweets;";
            }
            else{
                selectTags = "select score from tweets where tag1 =  '"+tagToSearch+"' ";
                for (int i=1; i<numberOfTags; i++){
                    String num = Integer.toString(i+1);
                    selectTags += " OR tag" + num + " = '"+tagToSearch+"'";
                }
            }
            try {
                Connection con = getConnection();
                Statement stmt = con.createStatement();
                rs = stmt.executeQuery(selectTags);           

            } catch (Exception e){
                System.out.println("Exception in select " + e);
            }
        }
        
        if (rs != null){
            
                        
            try {
                while (rs.next()){
                    int score = rs.getInt(1);
                    if (score >= 0 && score <5){

                        result[score]++;
                        result[5]++; //count all scores
                        result[6]+=score; //sum of all scores
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(JavaTweet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
        return result;
    }
    
    
    
    private static TreeNode recursiveSearch(String s, TreeNode node){
    
    if (node.getTag() == null ? s == null : node.getTag().equals(s)) {
        return node;
    }
    else {   
        List<TreeNode> children = node.getChildren(); 
        TreeNode result = null;

        for (int i = 0; result == null && i < children.size(); i++) {         
            result = recursiveSearch(s, children.get(i));
        }
        return result;
    }
    
 }       
    
    
    /*public static String cleanText(String text) {
        //text = text.replace("\n", "\\n");
        //text = text.replace("\t", "\\t");
        text = text.replaceAll("[^a-zA-Z0-9#@_]+"," ");
        return text;
     }*/
}
