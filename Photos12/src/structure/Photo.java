package structure;

import javafx.scene.image.Image;

import java.io.*;
import java.util.*;
/**
 * The Photo class
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class Photo implements java.io.Serializable {
    /**
     * serialVersion UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * the last modified date of photo
     */
    public Calendar date;

    /**
     * the caption of the photo
     */
    public String caption;

    /**
     * the path of the photo
     */
    public String path;

    /**
     * the tags of the photo
     */
    public HashMap<String, ArrayList<String>> tags=new HashMap<String, ArrayList<String>>();

    /**
     * This constructor is going to initialize the Photo class
     * @param file the image file
     */
    public Photo(File file){
        this.path=file.getPath();
        file.lastModified();
        date = Calendar.getInstance();
        date.set(Calendar.MILLISECOND,0);
        date.setTimeInMillis(file.lastModified());
    }

    /**
     * This constructor is going to make a copy of photo p
     * @param p
     */
    public Photo(Photo p) {
        this.caption=p.caption;
        this.date=p.date;
        this.tags=new HashMap<String, ArrayList<String>>();
        for(Map.Entry<String, ArrayList<String>> e:p.tags.entrySet()){
            ArrayList<String> temp=new ArrayList<>();
            for(String str:e.getValue()){
                temp.add(str);
            }
            this.tags.put(e.getKey(),temp);
        }
        this.path=p.path;
    }

    /**
     * Get the earliest photo from the input ArrayList
     * @param photos The list of photo
     * @return The date as format "month/day/year"
     */
    public static String getEarlistDate(ArrayList<Photo> photos){
        if(photos==null||photos.isEmpty())return null;
        Calendar temp=photos.get(0).date;
        long min=Long.MAX_VALUE;
        for(Photo p:photos){
            if(min>=p.date.getTimeInMillis()){
                min=p.date.getTimeInMillis();
                temp=p.date;
            }
        }

        return (temp.get(Calendar.MONTH)+1)+"/"+temp.get(Calendar.DATE)+"/"+temp.get(Calendar.YEAR);
    }

    /**
     * Get the most recent photo from the input ArrayList
     * @param photos The list of photo
     * @return The date as format "month/day/year"
     */
    public static String getRecenttDate(ArrayList<Photo> photos){
        if(photos==null||photos.isEmpty())return null;
        Calendar temp=photos.get(0).date;
        long max=Long.MIN_VALUE;
        for(Photo p:photos){
            if(max<=p.date.getTimeInMillis()){
                max=p.date.getTimeInMillis();
                temp=p.date;
            }
        }

        return (temp.get(Calendar.MONTH)+1)+"/"+temp.get(Calendar.DATE)+"/"+temp.get(Calendar.YEAR);
    }

    /**
     * Retrieve the image by path
     * @return
     */
    public Image getImage(){
        File file = new File(path);
        return new Image(file.toURI().toString());
    }



}
