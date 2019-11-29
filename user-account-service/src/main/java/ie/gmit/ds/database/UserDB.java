package ie.gmit.ds.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ie.gmit.ds.api.User;

public class UserDB {
 
    public static HashMap<Integer, User> users = new HashMap<>();
    
    static{
        users.put(1, new User(1, "user1", "user1@email.com", "can't"));
        users.put(2, new User(2, "user2", "user2@email.com", "cannot"));
        users.put(3, new User(3, "user3", "user3@email.com", "unabletocan"));
    }
     
    public static List<User> getUsers(){
    	// An ArrayList will return as a JSON array if displayed
        return new ArrayList<User>(users.values());
    }
     
    // Return user
    public static User getUser(int userID){
        return users.get(userID);
    }
    
    // Update or create a new user
    public static void updateUser(int id, User user){
        users.put(id, user);
    }
     
    // Delete user
    public static void removeUser(int userID){
        users.remove(userID);
    }
}
