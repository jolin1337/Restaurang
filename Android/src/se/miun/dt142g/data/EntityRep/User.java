/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.data.EntityRep;

/**
 *
 * @author Ali Omran
 */
public class User {
    
    final private int id;
    private String username;
    private String password;
    private String phonenr;
    private String mail;
    private boolean removeFlag = false;
    
    public User(int id, String username, String password, String mail, String telenr){
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phonenr = telenr;
        
    }
    
    public int getId(){
        return id;
    }
    
    public void setUsername(String un){
        username = un;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String pw){
        password = pw;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setMail(String mail){
        this.mail = mail;
    }
    
    public String getMail(){
        return mail;
    }
    
    public void setPhoneNumber(String phonenr){
        this.phonenr =phonenr;
    }
    
    public String getPhoneNumber(){
        return phonenr; 
    }
    public void setRemove(){
        removeFlag = true;
    }
    
    public boolean isRemovable(){
        return removeFlag;
    }
            
}