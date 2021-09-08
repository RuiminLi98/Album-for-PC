package structure;
import java.io.*;
import java.util.*;

/**
 * The Album class provide method to add album, delete album, rename album, and stream store for album
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class Album implements Serializable{
    /**
     * The name of the current album
     */
    public static String currentAlbumName;
    /**
     * The list of all of the album in this user account
     */
    public static ArrayList<Album> albums= new ArrayList<Album>();
    /**
     * storeDir String store the "dat"
     */
    public static final String storeDir = "dat";
    /**
     * storeDir String store the "Albums.dat"
     */
    public static String storeFile = User.CurrentUsername+"'s Albums.dat";
    /**
     * The ID of the serial version
     */
    private static final long serialVersionUID = 6725773749554546879L;

    /**
     * The function help us to add a new album
      * @param album The name of the new album
     * @return Return true if we add it successfully
     */
    public static boolean addAlbum(Album album)
    {
        if(albums==null){
            albums= new ArrayList<Album>();
        }
        if(albums.contains(new Album(album.name)))
            return false;
        else
            albums.add(album);
        return true;
    }

    /**
     * This function help us to delete the album
     * @param targetAlbum The name of the album that we want to delete
     * @return Return true if we delete it successfully
     */
    public static boolean deleteAlbum(Album targetAlbum)
    {
        if(albums==null){
            albums= new ArrayList<Album>();
        }
        if(!(albums.contains(new Album(targetAlbum.name))))
            return false;
        else 
            albums.remove(targetAlbum);
        return true;
    }

    /**
     * The name of the album
     */
    public String name;
    /**
     * The photos list of the current album
     */
    public ArrayList<Photo> photos=new ArrayList<Photo>();
    /**
     * The newest tiem of the album
     */
    public String earliestTime;
    /**
     * The latest time of the album
     */
    public String latestTime;

    /**
     * The constructor of the album class
     * @param name The name of the album
     */
    public Album(String name)
    {
        this.name=name;
    }

    /**
     * This function help us to rename the album
     * @param oldName The old name of the album
     * @param newName The new name of the album
     * @return Return true is the album be renamed successfully
     */
    public static boolean reNameAlbum(String oldName, String newName)
    {
        if(newName==null || newName.trim().isEmpty() || oldName==null || oldName.trim().isEmpty() || albums.contains(new Album(newName)) || !(albums.contains(new Album(oldName))) )
            return false;
        else
        {
            Album t;
            for(int i=0;i<albums.size();i++){
                t=albums.get(i);
                if(t.name.equals(oldName)){
                    albums.remove(i);
                    t.name=newName;
                    albums.add(t);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * juage if two objects are equal to each other
     * @param obj the target object
     * @return True if the two objects are the same
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Album ))
            return false;
        if(this==obj)
            return true;
        Album o=(Album) obj;
        return o.name.equals(this.name);
    }

    /**
     * Write the new albums information to the database
     * @throws IOException
     */
    public static void writeAlbums()throws IOException {
        storeFile = User.CurrentUsername+"'s Albums.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(albums);
    }

    /**
     * Read the albums of the user in the database
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void readAlbums() throws IOException, ClassNotFoundException {
        storeFile = User.CurrentUsername+"'s Albums.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        Album.albums = (ArrayList<Album>) ois.readObject();
    }
}
