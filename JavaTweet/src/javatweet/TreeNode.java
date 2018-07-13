/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javatweet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Anna
 */
//implements Iterable<TreeNode<T>>
public class TreeNode  {

    //private Datas datas;
    private String tag;
    private int value;
    private int[] scores;
    TreeNode parent;
    List<TreeNode> children;

    public TreeNode(String s, int v, int[] score) {
        this.tag = s;
        this.value = v;
        this.scores = score;
        this.children = new LinkedList<TreeNode>();
    }

    public String getTag(){
        return tag;
    }
    public String getValue(){
        return Integer.toString(value);
    }
    public int[] getScores(){
        return scores;
    }
    public TreeNode getParent(){
        if (parent != null){
                return parent;
        }
        else 
        return new TreeNode ("noparent", 0, null);
    }
    public void addChild(String s, int v, int[] scores) {
        TreeNode childNode = new TreeNode(s,v,scores);
        
        childNode.parent = this;
        this.children.add(childNode);
        //return childNode;
    } 
     
    public List<TreeNode> getChildren (){
        return children;
    }
       
    
    
    // other features ...

}