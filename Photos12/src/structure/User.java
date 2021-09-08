package structure;
import java.io.*;
import java.util.ArrayList;

/**
 * The user class
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class User implements Serializable {

    /**
     * storeDir String store the "dat"
     */
    public static final String storeDir = "dat";

    /**
     * storeDir String store the "users.dat"
     */
    public static final String storeFile = "users.dat";

    /**
     * CurrentUsername is used to store the username of current user
     */
    public static String CurrentUsername;

    /**
     * serialVersion UID
     */
    private static final long serialVersionUID = -726758312738770277L;

    /**
     * username String, should be used when logging in
     */
    public String username;

    /**
     * users ArrayList, contains the list of existing users. This list should be load every time when the program starts.
     */
    public static ArrayList<User> users;

    /**
     * This method is used to initialize the user
     * @param username
     */
    public User(String username){
        this.username=username;
    };

    /**
     * This method is used to decide whether two Users are same.
     * @param obj
     * @return true if the objects have same username
     */
    @Override
    public boolean equals(Object obj) {
        if(obj==null||!(obj instanceof User)){
            return false;
        }
        if(this==obj)return true;
        User o=(User)obj;
        return o.username.equals(this.username);
    }

    /**
     * This method is going to store the data of current users in Serialization
     * @throws IOException
     */
    public static void writeUsers()throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(users);
    }

    /**
     * This method is going to read the data of users in Serialization
     * @throws IOException
     */
    public static void readUsers() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
         User.users = (ArrayList<User>) ois.readObject();
    }
}
