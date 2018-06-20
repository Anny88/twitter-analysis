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
    TreeNode parent;
    List<TreeNode> children;

    public TreeNode(String s, int v) {
        this.tag = s;
        this.value = v;
        this.children = new LinkedList<TreeNode>();
    }

    public String getTag(){
        return tag;
    }
    public String getValue(){
        return Integer.toString(value);
    }
    public TreeNode getParent(){
        if (parent != null){
                return parent;
        }
        else 
        return new TreeNode ("noparent", 0);
    }
    public void addChild(String s, int v) {
        TreeNode childNode = new TreeNode(s,v);
        
        childNode.parent = this;
        this.children.add(childNode);
        //return childNode;
    } 
     
    public List<TreeNode> getChildren (){
        return children;
    }
       
    
    
    // other features ...

}