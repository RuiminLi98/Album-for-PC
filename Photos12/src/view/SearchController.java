package view;


import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import app.Photos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.w3c.dom.Text;
import structure.Album;
import structure.Photo;
import structure.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * The Search controller
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class SearchController extends Controller{

    /**
     * the ObservableList contains the photo Entries
     */
    ObservableList<Entry> observableList;
    /**
     * This textfield is the input place for tag name of the single tag search
     */
    @FXML
    TextField TagSingleName;
    /**
     * This textfield is the input place for tag value of the single tag search
     */
    @FXML
    TextField TagSingleValue;
    /**
     * This textfield is the input place for the first tag name of the double tag search
     */
    @FXML
    TextField TagDoubleName1;
    /**
     * This textfield is the input place for the first tag value of the double tag search
     */
    @FXML
    TextField TagDoubleValue1;
    /**
     * This textfield is the input place for the second tag name of the double tag search
     */
    @FXML
    TextField TagDoubleName2;
    /**
     * This textfield is the input place for the second tag place of the double tag search
     */
    @FXML
    TextField TagDoubleValue2;
    /**
     * This textfield store the newablbum name we created for the search result
     */
    @FXML
    TextField NewAlbumName;
    /**
     * This button create the new album for the search result
     */
    @FXML
    Button CreateNewAlbum;
    /**
     * This button search by single tag
     */
    @FXML
    Button SearchSingle;
    /**
     * This button search by conjunction of two tags
     */
    @FXML
    Button SearchConjunction;
    /**
     * This button search by disjunction of two tags
     */
    @FXML
    Button SearchDisjunctive;
    /**
     * This listview show all the images of the search result
     */
    @FXML
    TableView FoundPhotoList;
    /**
     * This textfield store the minimum time for the time search
     */
    @FXML
    TextField minTime;
    /**
     * This textfield store the meximum time of the time search
     */
    @FXML
    TextField maxTime;
    /**
     * This button implement time search
     */
    @FXML
    Button SearchByTime;
    /**
     * The photos' search result
     */
    public static ArrayList<Photo> thisSearchResult=new ArrayList<Photo>();
    /**
     * The temp store variable of the photos' search result
     */
    public static ArrayList<Photo> tempChooseingPhoto=new ArrayList<Photo>();



    /**
     * The inner class that hold the image and caption for one photo
     */
    public class Entry{
        ImageView imageView;
        String caption;
        public Entry(ImageView imageView, String caption){
            this.imageView=imageView;
            this.caption=caption;
        }

        public String getCaption(){
            return this.caption;
        }

        public ImageView getImageView(){return this.imageView;}

    }

    /**
     * the ObservableList contains the photo Entries
     */
    ObservableList<Entry> photoEntryList;


    /**
     * This method help us to show all the photos be searched out
     */
    private void loadList() {

        if(FoundPhotoList.getColumns().size()!=2) {
            TableColumn imageCol = new TableColumn("Photo");
            imageCol.setMinWidth(250);
            imageCol.setSortable(false);
            imageCol.setCellValueFactory(new PropertyValueFactory<>("imageView"));
            TableColumn captionCol = new TableColumn("Caption");
            captionCol.setMinWidth(136);
            captionCol.setSortable(false);
            captionCol.setCellValueFactory(new PropertyValueFactory<>("caption"));
            FoundPhotoList.getColumns().addAll(imageCol,captionCol);
        }
        photoEntryList = FXCollections.observableArrayList ();
        if(thisSearchResult==null){
            thisSearchResult=new ArrayList<>();
        }

        for(Photo p:thisSearchResult){
            ImageView imageView = new ImageView(p.getImage());
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            photoEntryList.add(new Entry(imageView,p.caption));
        }
        FoundPhotoList.setItems(photoEntryList);

    }

    /**
     * The method change the string to lowercase
     * @param a The given string
     * @return The string transfered to lowercase
     */
    public ArrayList<String> changeToLowercase(ArrayList<String> a)
    {
        ArrayList<String> b=new ArrayList<String>();
        b.addAll(changeToLowercase(a));
        return b;
    }

    /**
     * This method implement conjunction search of two tags
     * @param actionEvent
     */
    public void searchConjunctionTapped(ActionEvent actionEvent) {
        thisSearchResult.clear();
        FoundPhotoList.getItems().clear();
        tempChooseingPhoto.clear();
        for(int i=0;i<Album.albums.size();i++)
        {
            for(int j=0;j<Album.albums.get(i).photos.size();j++)
            {
                if(Album.albums.get(i).photos.get(j).tags.containsKey(TagDoubleName1.getText().toLowerCase()))
                {
                    ArrayList<String> temp=Album.albums.get(i).photos.get(j).tags.get(TagDoubleName1.getText().toLowerCase());
                    if(temp.contains(TagDoubleValue1.getText().toLowerCase()))
                    {
                        if(Album.albums.get(i).photos.get(j).tags.containsKey(TagDoubleName2.getText().toLowerCase()))
                        {
                            temp=Album.albums.get(i).photos.get(j).tags.get(TagDoubleName2.getText().toLowerCase());
                            if(temp.contains(TagDoubleValue2.getText().toLowerCase()))
                            {
                                thisSearchResult.add(Album.albums.get(i).photos.get(j));
                            }
                        }
                    }

                }
            }
        }
//        thisSearchResult=noRepeatPic(thisSearchResult);
        tempChooseingPhoto.addAll(thisSearchResult);
        loadList();
        thisSearchResult.clear();
        TagDoubleName1.setText("");
        TagDoubleName2.setText("");
        TagDoubleValue1.setText("");
        TagDoubleValue2.setText("");
    }

    /**
     * This method implement the search of single tag
     * @param actionEvent
     */
    public void searchSingleTapped(ActionEvent actionEvent) {         //albums是不是当前用户下的album列表
        thisSearchResult.clear();
        FoundPhotoList.getItems().clear();
        tempChooseingPhoto.clear();
        for(int i=0;i< Album.albums.size();i++)
        {
            for(int j=0;j<Album.albums.get(i).photos.size();j++)
            {
                if(Album.albums.get(i).photos.get(j).tags.containsKey(TagSingleName.getText().toLowerCase()))
                {
                    ArrayList<String> temp=Album.albums.get(i).photos.get(j).tags.get(TagSingleName.getText().toLowerCase());
                    if(temp.contains(TagSingleValue.getText().toLowerCase()))
                    {
                        thisSearchResult.add(Album.albums.get(i).photos.get(j));
                    }
                }
            }
        }
//        thisSearchResult=noRepeatPic(thisSearchResult);
        tempChooseingPhoto.addAll(thisSearchResult);
        loadList();
        thisSearchResult.clear();
        TagSingleName.setText("");
        TagSingleValue.setText("");
    }

    /**
     * This method implement disjunctive search for two tags
     * @param actionEvent
     */
    public void searchDisjunctiveTapped(ActionEvent actionEvent) {
        thisSearchResult.clear();
        FoundPhotoList.getItems().clear();
        tempChooseingPhoto.clear();
        boolean flag=false;
        for(int i=0;i<Album.albums.size();i++)
        {
            for(int j=0;j<Album.albums.get(i).photos.size();j++)
            {

                if(Album.albums.get(i).photos.get(j).tags.containsKey(TagDoubleName1.getText().toLowerCase())) {
                    ArrayList<String> temp = Album.albums.get(i).photos.get(j).tags.get(TagDoubleName1.getText().toLowerCase());
                    if (temp.contains(TagDoubleValue1.getText().toLowerCase())) {
                        thisSearchResult.add(Album.albums.get(i).photos.get(j));
                        flag=true;
                    }
                }
                if(Album.albums.get(i).photos.get(j).tags.containsKey(TagDoubleName2.getText().toLowerCase()) && !flag)
                {
                    ArrayList<String> temp=Album.albums.get(i).photos.get(j).tags.get(TagDoubleName2.getText().toLowerCase());
                    if(temp.contains(TagDoubleValue2.getText().toLowerCase()))
                    {
                        thisSearchResult.add(Album.albums.get(i).photos.get(j));
                    }
                }
                flag=false;
            }
        }
//        thisSearchResult=noRepeatPic(thisSearchResult);
        tempChooseingPhoto.addAll(thisSearchResult);
        loadList();
        thisSearchResult.clear();
        TagDoubleName1.setText("");
        TagDoubleName2.setText("");
        TagDoubleValue1.setText("");
        TagDoubleValue2.setText("");
    }

    /**
     * This method help us return to the userdashboard page
     * @param actionEvent
     */
    public void backTapped(ActionEvent actionEvent) {
        thisSearchResult.clear();
        tempChooseingPhoto.clear();
        try {
            User.writeUsers();
            Album.writeAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/UserDashboard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle(User.CurrentUsername+"'s Home");
        Photos.stage.setScene(new Scene(root, 900, 600));
        Photos.stage.show();
    }

    /**
     * This method help us to logout
     * @param actionEvent
     */
    public void logoutTapped(ActionEvent actionEvent) {
        thisSearchResult.clear();
        tempChooseingPhoto.clear();
        try {
            User.writeUsers();
            Album.writeAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle("Log in");
        Photos.stage.setScene(new Scene(root, 450, 300));
        Photos.stage.show();
    }

    /**
     * This method implement the function of creating new album for the search result
     * @param actionEvent
     */
    public void createNewAlbumtapped(ActionEvent actionEvent) {
        String name = NewAlbumName.getText().trim();
        if (name == null || name.isEmpty()){
            showError("Illegal Album name","Please try again!");
            NewAlbumName.setText("");
            return;
        }else if(!Album.addAlbum(new Album(name))){
            showError("Illegal Album name","Please try again!");
            NewAlbumName.setText("");
            return;
        }
        int num=Album.albums.indexOf(new Album(name));
        for(int i=0;i<tempChooseingPhoto.size();i++)
        {
            Photo t=new Photo(tempChooseingPhoto.get(i));
            Album.albums.get(num).photos.add(t);
        }
        NewAlbumName.setText("");
    }

    /**
     * This method implement the function of search by time
     * @param actionEvent
     */
    public void searchByTimeTapped(ActionEvent actionEvent) throws ParseException {
        thisSearchResult.clear();
        FoundPhotoList.getItems().clear();
        tempChooseingPhoto.clear();
        String[] min=minTime.getText().split("/");
        String[] max=maxTime.getText().split("/");
        if(min.length!=3 || max.length!=3) {
            showError("Bad Input", "Please input legal time format");
            minTime.setText("");
            maxTime.setText("");
            return;
        }
        int m1=Integer.parseInt(min[0]);
        int m2=Integer.parseInt(max[0]);
        int d1=Integer.parseInt(min[1]);
        int d2=Integer.parseInt(max[1]);
        if(m1>12 || m2>12 || d1>31 || d2>31) {
            showError("Bad Input", "Please input reasonable data");
            minTime.setText("");
            maxTime.setText("");
            return;
        }
        String str1=min[2]+"-"+min[0]+"-"+min[1];
        String str2=max[2]+"-"+max[0]+"-"+max[1];
        SimpleDateFormat sdf=new SimpleDateFormat(("yyyy-MM-dd"));
        Date date1=sdf.parse(str1);
        Date date2=sdf.parse(str2);
        Calendar calendar1=Calendar.getInstance();
        Calendar calendar2=Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        calendar2.add(Calendar.DATE,1);
        for(int i=0;i<Album.albums.size();i++) {
            for (int j = 0; j < Album.albums.get(i).photos.size(); j++) {
                Calendar c = Album.albums.get(i).photos.get(j).date;
                long cc = c.getTimeInMillis();
                if (cc >= calendar1.getTimeInMillis() && cc <= calendar2.getTimeInMillis())
                    thisSearchResult.add(new Photo(Album.albums.get(i).photos.get(j)));
            }

        }
//        thisSearchResult=noRepeatPic(thisSearchResult);
        tempChooseingPhoto.addAll(thisSearchResult);
        loadList();
        thisSearchResult.clear();
        minTime.setText("");
        maxTime.setText("");
    }
}
