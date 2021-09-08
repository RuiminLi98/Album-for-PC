package view;


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
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import structure.Album;
import structure.Photo;
import structure.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The User Dash Board controller
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class UserDashController extends Controller{

    /**
     * The TextField contains the album's name to be opened
     */
    @FXML
    TextField AlbumOpen;

    /**
     * The TextField contains the new album's name during changing album's name
     */
    @FXML
    TextField NewNameTextField;

    /**
     * The TextField contains the old album's name during changing album's name
     */
    @FXML
    TextField OldAlbumTextField;

    /**
     * The TextField contains the existing album's name to be deleted
     */
    @FXML
    TextField DeleteAlbumTextField;

    /**
     * The TextField contains the new album's name to be added
     */
    @FXML
    TextField AddAlbumTextField;

    /**
     * The TextField contains the old album's name during union of two albums
     */
    @FXML
    TextField OldAlbumATextField;

    /**
     * The TextField contains the new album's name during union of two albums
     */
    @FXML
    TextField NewAlbumNameTextField;

    /**
     * The TextField contains the old album's name during union of two albums
     */
    @FXML
    TextField OldAlbumBTextField;

    /**
     * The list view contains the album list
     */
    @FXML
    ListView<String> AlbumList;

    /**
     * This method is going to initialize the user dash page by reading saved data from local disk and set stock album
     */
    @FXML
    void initialize(){
        try {
            Album.readAlbums();
        } catch (Exception e) {

            if(Album.albums==null)
                Album.albums=new ArrayList<>();
            Album stock=new Album("stock");
            stock.photos=new ArrayList<>();
            Photo p1=new Photo(new File("stock/a.jpeg"));
            p1.caption="Photo A";
            stock.photos.add(p1);
            Photo p2=new Photo(new File("stock/b.jpeg"));
            p2.caption="Photo B";
            stock.photos.add(p2);
            stock.photos.add(new Photo(new File("stock/c.jpeg")));
            stock.photos.add(new Photo(new File("stock/d.jpeg")));
            stock.photos.add(new Photo(new File("stock/e.jpeg")));
            stock.photos.add(new Photo(new File("stock/f.jpeg")));
            Album.albums.add(stock);
        }
        loadList();
        AlbumList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            /**
             * Called when the value of an {@link ObservableValue} changes.
             * <p>
             * In general, it is considered bad practice to modify the observed value in
             * this method.
             *
             * @param observable The {@code ObservableValue} which value changed
             * @param oldValue   The old value
             * @param newValue
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int index=AlbumList.getSelectionModel().getSelectedIndex();
                if(Album.albums!=null&&index>=0&&index<Album.albums.size()){
                    String str=Album.albums.get(index).name;
                    DeleteAlbumTextField.setText(str);
                    OldAlbumTextField.setText(str);
                    AlbumOpen.setText(str);
                }else{
                    DeleteAlbumTextField.setText("");
                    OldAlbumTextField.setText("");
                    AlbumOpen.setText("");
                }
            }
        });
    }

    /**
     * This method is used to load the album list.
     */
    private void loadList() {
        ObservableList observableList = FXCollections.observableArrayList ();
        if(Album.albums!=null){
            Collections.sort(Album.albums, new Comparator<Album>() {
                @Override
                public int compare(Album o1, Album o2) {
                    return o1.name.compareTo(o2.name);
                }
            });
            for(Album u:Album.albums){
                String str;
                int size=u.photos==null?0:u.photos.size();
                String earlist=Photo.getEarlistDate(u.photos);
                String recent=Photo.getRecenttDate(u.photos);
                if(earlist==null||recent==null){
                    str=u.name+"\tTotal: "+size+" photos";
                }else{
                    str=u.name+"\tTotal: "+size+" photos\t  Date Range:"+ earlist+" to "+recent;
                }

                observableList.add(str);
            }
            AlbumList.setItems(observableList);
        }
    }

    /**
     * The add new album button is tapped
     * @param actionEvent
     */
    public void addTapped(ActionEvent actionEvent) {
        String name = AddAlbumTextField.getText().trim();
        if (name == null || name.isEmpty()){
            showError("Illegal Album name","Please try again!");
        }else if(!Album.addAlbum(new Album(name))){
            showError("Illegal Album name","Please try again!");
        }
        loadList();
        AddAlbumTextField.setText("");

    }

    /**
     * The delete existing album button is tapped
     * @param actionEvent
     */
    public void deleteTapped(ActionEvent actionEvent) {
        String name = DeleteAlbumTextField.getText().trim();
        if (name == null || name.isEmpty()){
            showError("Illegal Album name","Please try again!");
        }else if(! Album.deleteAlbum(new Album(name))){
            showError("Illegal Album name","Please try again!");
        }
        DeleteAlbumTextField.setText("");
        OldAlbumTextField.setText("");
        AlbumOpen.setText("");
        AlbumList.getSelectionModel().select(-1);
        loadList();
    }

    /**
     * The rename existing album button is tapped
     * @param actionEvent
     */
    public void renameTapped(ActionEvent actionEvent) {

        if(!Album.reNameAlbum(OldAlbumTextField.getText(),NewNameTextField.getText())) {
            showError("Illegal Album name","Please try again!");
        }
        OldAlbumTextField.setText("");
        NewNameTextField.setText("");
        DeleteAlbumTextField.setText("");
        AlbumOpen.setText("");
        AlbumList.getSelectionModel().select(-1);
        loadList();
    }

    /**
     * The open existing album button is tapped
     * @param actionEvent
     */
    public void openTapped(ActionEvent actionEvent) {
        String name = AlbumOpen.getText().trim();
        if (name == null || name.isEmpty()||!Album.albums.contains(new Album(name))){
            showError("Illegal Album name","Please try again!");
        }else{
            Album.currentAlbumName=name;
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../view/Album.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Photos.stage.setTitle(name);
            Photos.stage.setScene(new Scene(root, 900, 600));
            Photos.stage.show();
        }
        AlbumOpen.setText("");
        DeleteAlbumTextField.setText("");
        OldAlbumTextField.setText("");
        AlbumList.getSelectionModel().select(-1);
    }

    /**
     * The button is tapped to jump to search page
     * @param actionEvent
     */
    public void SearchTapped(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/SearchPage.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle("Search Page");
        Photos.stage.setScene(new Scene(root, 900, 600));
        Photos.stage.show();
    }


    /**
     * The method to union two old album to a new one
     * @param actionEvent
     */
    public void UnionTapped(ActionEvent actionEvent) {
        String name1=OldAlbumATextField.getText();
        String name2=OldAlbumBTextField.getText();
        String name3=NewAlbumNameTextField.getText();
        if(judgeCondition(name1,name2,name3))
        {
            int num1=0;
            int num2=0;
            for(int i=0;i<Album.albums.size();i++)
            {
                if(Album.albums.get(i).name.equals(name1))
                {
                    num1=i;
                }
                if(Album.albums.get(i).name.equals(name2))
                {
                    num2=i;
                }
            }
            Album newone=Union(Album.albums.get(num1),Album.albums.get(num2),name3);
            Album.deleteAlbum(Album.albums.get(num1));
            for(int i=0;i<Album.albums.size();i++)
            {
                if(Album.albums.get(i).name.equals(name2))
                {
                    num2=i;
                }
            }
            Album.deleteAlbum(Album.albums.get(num2));
            Album.albums.add(newone);
        }
        OldAlbumATextField.setText("");
        OldAlbumBTextField.setText("");
        NewAlbumNameTextField.setText("");
        loadList();
    }

    /**
     * The method to judge if the union method have a right input information
     * @param name1 The first old album name
     * @param name2 The second old album name
     * @param name3 The name album name
     * @return If the input is acceptable, then return true. Otherwise, return false
     */
    public boolean judgeCondition(String name1,String name2, String name3){
        int flag1=0;
        int flag2=0;
        if(name1.equals(name2))
        {
            String header="The two old album can not have same name";
            String mess="Please change the old album name";
            showError(header,mess);
            return false;
        }
        for(int i=0;i<Album.albums.size();i++)
        {
            if(name1.equals(Album.albums.get(i).name))
                flag1=1;
            if(name2.equals((Album.albums.get(i).name)))
                flag2=1;
        }
        if(flag1==0 || flag2==0)
        {
            showError("Old Album Name do not exist","Please try to use existed album");
            return false;
        }
        flag1=0;
        flag2=0;
        for(int i=0;i<Album.albums.size();i++)
        {
            if(name3.equals(Album.albums.get(i).name) && !(name3.equals(name1)) && !(name3.equals(name2)))
            {
                showError("The target album name need be a new name or one of old album name","Please try again");
                return false;
            }
        }
        return true;
    }

    /**
     * The method union the album and their photo
     * @param a The first old album name
     * @param b The second old album name
     * @param name The name of the name Album
     * @return return to the new album that we created
     */
    public Album Union(Album a,Album b, String name){
        ArrayList<Photo> temp= new ArrayList<Photo>();
        for(int i=0;i<a.photos.size();i++){
            temp.add(new Photo(a.photos.get(i)));
        }
        for(int i=0;i<b.photos.size();i++){
            temp.add(new Photo(b.photos.get(i)));
        }
        Album tar=new Album(name);
        tar.photos=temp;
        return tar;
    }

}
