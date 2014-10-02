/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miun.dt142g.user;

import miun.dt142g.data.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miun.dt142g.DataSource;


/**
 *
 * @author Ali Omran
 */
public class Users extends DataSource implements Iterable<User>{
    
    private List<User> users = new ArrayList<>();
    
    public Users(){     
    }
    
    public int getRows(){
        return users.size();
    }
    
    public User getUser(int id){
        for(User u: users)
        {
            if(u.getId() == id)
                return u;
        }
        return null;
    }
    public void addUser(User user){
        users.add(user);
    }
    
    public void removeUser(int id){
        users.remove(this.getUser(id));
    }
    
    @Override
    public void loadData(){
        
    }
    
    @Override
    public void update(){
        
    }
    
    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }
    
    @Override
    public int getUniqueId(){
        int id = 0;
        for(User u: users){
            if(u.getId() > id)
                id = u.getId()+1;
        }
        return id;
    }
    
    
    
    
    
    
}
