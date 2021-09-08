package structure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The admin class provides the functions of listing users, create new users, and delete existing users.
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class Admin {

    /**
     * This method is going to list users
     * @return A list of users
     */
    public static List<User> getUsers(){
        return User.users;
    }

    /**
     * This method is going to create a new user into the User Library
     * @param username
     * @return True if create successfully. Otherwise, return false.
     */
    public static boolean createUser(String username){
        if(User.users.contains(new User(username))||username==null||username.trim().isEmpty()||username.trim().equals("null"))return false;
        User.users.add(new User(username));
        return true;
    }

    /**
     * This method is going to delete a user existing in the User Library
     * @param username
     * @return True if delete successfully. Otherwise, return false.
     */
    public static boolean deleteUser(String username){
        username=username.trim();
        if(!User.users.contains(new User(username)))return false;
        User.users.remove(new User(username));
        try {
            String storeDir = "dat";
            String storeFile = username+"'s Albums.dat";
            Files.deleteIfExists(Paths.get(storeDir + File.separator + storeFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
