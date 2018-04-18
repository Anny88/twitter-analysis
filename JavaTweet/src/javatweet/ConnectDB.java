/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
                     + "maintweet VARCHAR(280), "
                     + "tag1 VARCHAR(280), "
                     + "tag2 VARCHAR(280),"
                     + "tag3 VARCHAR(280),"
                     + "tag4 VARCHAR(280),"
                     + "tag5 VARCHAR(280),"
                     + "PRIMARY KEY (id) )";
             
             PreparedStatement create = con.prepareStatement(createTable);
             create.executeUpdate();
         }
         catch (Exception e){
             System.out.println(e);
         }
         finally {System.out.println("Function complete");}
     }
     
     public static void post(String main, List tags) throws Exception{
         //final String main = "@RT jkfdfldfsdf #haha #puuf";
         //final String tag1 = "#haha";
         try{
             Connection con = getConnection();
             String insert = "INSERT INTO tweets(maintweet";
             String values = " VALUES ('"+main+"'";
             for (int i = 0; i<tags.size(); i++){
                String j = Integer.toString(i+1);
                insert += ", tag" + j;
                values += ", '"+tags.get(i)+"' ";
             }
                        
             insert += ")";
             values += ")";
             
             PreparedStatement posted = con.prepareStatement(insert+values);
             posted.executeUpdate();
         }
         catch (Exception e){
             System.out.println(e);
         }
         //finally {System.out.println("Insert complete");}
     }
     public static void drop() throws Exception{
         //final String main = "@RT jkfdfldfsdf #haha #puuf";
         //final String tag1 = "#haha";
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
         
     }
