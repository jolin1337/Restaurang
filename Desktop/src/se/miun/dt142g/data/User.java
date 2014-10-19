package se.miun.dt142g.data;

/**
 * This describes a user/waiter/employee at the company
 * 
 * @author Ali Omran
 */
public class User {
    
    /**
     * Identifier of a user
     */
    final private int id;
    /**
     * The username of this user
     */
    private String username;
    /**
     * The password for this user
     */
    private String password;
    /**
     * The phone number information of this user
     */
    private String phonenr;
    /**
     * The mail adress of this user
     */
    private String mail;
    /**
     * A remove flag that indicates if this user should be deleted
     */
    private boolean removeFlag = false;
    
    /**
     * Constructs an User object with the parameters as initial parameters
     * @param id
     * @param username
     * @param password
     * @param mail
     * @param telenr 
     */
    public User(int id, String username, String password, String mail, String telenr){
        this.id = id;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.phonenr = telenr;
        
    }
    
    /**
     * Getter for identifier of this user
     * @return The identifier of this user (PK)
     */
    public int getId(){
        return id;
    }
    
    /**
     * Setter of username
     * @param un The username this user should have
     */
    public void setUsername(String un){
        username = un;
    }
    
    /**
     * Getter of the username
     * @return The username of this user
     */
    public String getUsername(){
        return username;
    }
    
    /**
     * Setter of password
     * @param pw The password of this user
     */
    public void setPassword(String pw){
        password = pw;
    }
    
    /**
     * Getter of the password
     * @return the password this user has
     */
    public String getPassword(){
        return password;
    }
    
    /**
     * Setter of the mail adress of this user
     * @param mail The mail
     */
    public void setMail(String mail){
        this.mail = mail;
    }
    
    /**
     * Getter for the mail
     * @return The mail of this user
     */
    public String getMail(){
        return mail;
    }
    
    /**
     * Setter of this user phone number
     * @param phonenr The phone number
     */
    public void setPhoneNumber(String phonenr){
        this.phonenr =phonenr;
    }
    
    /**
     * The getter of the phone number of this user
     * @return The phone number
     */
    public String getPhoneNumber(){
        return phonenr; 
    }
    
    /**
     * Setter of the remove flag
     */
    public void setRemove(){
        removeFlag = true;
    }
    
    public boolean isRemoved(){
        return removeFlag;
    }
            
}
