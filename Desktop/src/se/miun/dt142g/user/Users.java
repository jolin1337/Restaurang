/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.miun.dt142g.user;

import se.miun.dt142g.data.User;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.miun.dt142g.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A container for all users
 *
 * @author Ali Omran
 */
public class Users extends DataSource implements Iterable<User> {

    /**
     * A list to hold all users
     */
    private List<User> users = new ArrayList<>();
    /**
     * Json array to hold all users
     */
    private JSONArray jsonUsers = new JSONArray();
    JSONObject response;
    JSONArray data;

    /**
     * Default constructor
     */
    public Users() {
    }

    /**
     * Getter for the amount instances in users
     *
     * @return the amount of users in the list, eg. the amount of rows
     */
    public int getRows() {
        return users.size();
    }

    /**
     * Getter for users, retrieves a user by id
     *
     * @param id - The id of the requested user
     * @return a user with the specified id, otherwise null if not found
     */
    public User getUser(int id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    /**
     * Add user by specifying a user
     *
     * @param user - User to be added
     */
    public void addUser(User user) {
        users.add(user);
        listToJsonArray();
    }

    /**
     * Remove a user by id
     *
     * @param id - Id of the user to remove
     */
    public void removeUser(int id) {
        users.remove(this.getUser(id));
    }

    /**
     * Add a user into the list from json object
     *
     * @param jsonUser - Json object user to be added
     */
    public void addJsonUser(JSONObject jsonUser) {

        int id;
        String username;
        String password;
        String phonenr;
        String email;

        try {
            id = jsonUser.getInt("id");
            username = jsonUser.getString("username");
            password = jsonUser.getString("password");
            phonenr = jsonUser.getString("phonenr");
            email = jsonUser.getString("email");
            users.add(new User(id, username, password, email, phonenr));
        } catch (JSONException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a json array from the users list
     */
    public void listToJsonArray() {
        for (User u : users) {
            try {
                JSONObject jsonUser = new JSONObject();
                JSONObject dataElement = new JSONObject();
                if (u.isRemoved() == true) {
                    jsonUser.put("remove", true);
                }
                jsonUser.put("id", u.getId());
                jsonUser.put("username", u.getUsername());
                jsonUser.put("password", u.getPassword());
                jsonUser.put("email", u.getMail());
                jsonUser.put("phonenr", u.getPhoneNumber());

                dataElement.put("data", jsonUser);
                jsonUsers.put(dataElement);

            } catch (JSONException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void loadData() {
        response = null;
        JSONArray data = null;
        try {
            response = getJsonRequest("user");
            data = response.getJSONArray("data");
        } catch (JSONException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < data.length(); i++) {
            try {
                addJsonUser(data.getJSONObject(i));
            } catch (JSONException ex) {
                Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update() {
        JSONObject send = new JSONObject();

        jsonUsers = new JSONArray();
        listToJsonArray();

        try {
            send.put("data", jsonUsers);
        } catch (JSONException ex) {
            Logger.getLogger(Users.class.getName()).log(Level.SEVERE, null, ex);
        }

        String urlParams = "key=" + key + "&table=user&data=" + send.toString();
        System.out.println("Update status: " + getRequest("updaterow", urlParams));
        System.out.println(send.toString());
        //jsonUsers = new JSONArray();
        users = new ArrayList<>();
        loadData();
    }

    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }

    @Override
    public int getUniqueId() {
        return -1;
    }
}
