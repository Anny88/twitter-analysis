package javatweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
                     + "maintweet VARCHAR(280) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL, "
                     + "score int,"
                     + "tag1 VARCHAR(280), "
                     + "tag2 VARCHAR(280),"
                     + "tag3 VARCHAR(280),"
                     + "tag4 VARCHAR(280),"
                     + "tag5 VARCHAR(280),"
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
     
     public static void post(List <String> tweets) throws Exception{
         Connection con = getConnection();
         NLP.init();
         for (int i=0; i<tweets.size(); i++){
            String main = tweets.get(i);
            List <String> tags = getWords(main);
            int score = NLP.findSentiment(main);
            
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
     
     
     public static List getWords(String tweet) throws Exception{
        String[] subStr;
        List <String> tags = new ArrayList();
        String delimeter = " "; // Разделитель
        if (tweet.length() > 0){
            try{
                subStr = tweet.split(delimeter); 
                for(int i = 0; i < subStr.length; i++) { 
                    char first = subStr[i].charAt(0);
                    if (first == '#'){
                        tags.add(subStr[i]);
                    }
                }
            }
            catch (Exception e){
               System.out.println(e);  
            }
        }
        if (tags.isEmpty()) {
            //tags.add("notag");
        }
        return tags;
    }       
         
     }
