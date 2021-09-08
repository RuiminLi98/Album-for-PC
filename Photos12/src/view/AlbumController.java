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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import structure.Album;
import structure.Photo;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The Album page's controller
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */

public class AlbumController extends Controller{
    public static int currentPhotoIndex;

    /**
     * The current Album's name
     */
    @FXML
    Label AlbumName;

    /**
     * the list of photos in the album
     */
    @FXML
    TableView tableview;

    /**
     * the ObservableList contains the photo Entries
     */
    ObservableList<Entry> photoEntryList;

    /**
     * the caption of the selecting photo
     */
    @FXML
    TextField CaptionTextField;

    /**
     * the target album's name that the selecting will be moved into
     */
    @FXML
    TextField NewAlbumNameTextField;

    /**
     * the current album reference
     */
    public static Album currentAlbum;

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
     * The initialize method should be run at the beginning of the stage. This method helps to load the list and set some action listener.
     */
    @FXML
    void initialize(){
        AlbumName.setText(Album.currentAlbumName);
        for(Album t:Album.albums){
            if(t.name.equals(Album.currentAlbumName)){
                currentAlbum=t;
                break;
            }
        }
        loadList();
        currentPhotoIndex=-1;
        tableview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
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
            public void changed(ObservableValue<? extends Entry> observable, Entry oldValue, Entry newValue) {
                int index=tableview.getSelectionModel().getSelectedIndex();
                currentPhotoIndex=index;
                if(index<0){
                    CaptionTextField.setText("");
                }else{
                    CaptionTextField.setText(currentAlbum.photos.get(index).caption);
                }
            }
        });
    }

    /**
     * This method is used to load photo from ArrayList into list view
     */
    private void loadList() {
       if(tableview.getColumns().size()!=2) {
           TableColumn imageCol = new TableColumn("Photo");
           imageCol.setMinWidth(180);
           imageCol.setSortable(false);
           imageCol.setCellValueFactory(new PropertyValueFactory<>("imageView"));
           TableColumn captionCol = new TableColumn("Caption");
           captionCol.setMinWidth(136);
           captionCol.setSortable(false);
           captionCol.setCellValueFactory(new PropertyValueFactory<>("caption"));
           tableview.getColumns().addAll(imageCol,captionCol);
       }
        photoEntryList = FXCollections.observableArrayList();
        if(currentAlbum.photos==null){
            currentAlbum.photos=new ArrayList<>();
        }
        for(Photo p:currentAlbum.photos){
            ImageView imageView = new ImageView(p.getImage());
            imageView.setFitHeight(150);
            imageView.setFitWidth(150);
            photoEntryList.add(new Entry(imageView,p.caption));
        }
        tableview.setItems(photoEntryList);
    }

    /**
     * This method is going to add a new photo.
     * @param actionEvent
     */
    public void AddTapped(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("FileChooser");
        FileChooser fil_chooser = new FileChooser();
        File file = fil_chooser.showOpenDialog(stage);
        if(file==null)return;
        Photo p=new Photo(file);
        Album temp=null;
        for(int i=0;i<Album.albums.size();i++){
            Album t=Album.albums.get(i);
            if(t.equals(new Album(Album.currentAlbumName))){
                if(Album.albums.get(i).photos==null){
                    Album.albums.get(i).photos=new ArrayList<>();
                }
                Album.albums.get(i).photos.add(p);
                loadList();
                break;
            }
        }
    }

    /**
     * This method is going to delete an existing photo.
     * @param actionEvent
     */
    public void deleteTapped(ActionEvent actionEvent) {
        int target = tableview.getSelectionModel().getSelectedIndex();
        if(target<0){
            showError("Error!","You should select one photo from Photo List");
            return;
        }
        currentAlbum.photos.remove(target);
        loadList();

    }

    /**
     * This method is going to cation or re-caption a photo
     * @param actionEvent
     */
    public void updateCationTapped(ActionEvent actionEvent) {
        int index=tableview.getSelectionModel().getSelectedIndex();
        if(index<0){
            CaptionTextField.setText("");
            showError("Error!","You should select one photo from Photo List");
            return;
        }else{
            currentAlbum.photos.get(index).caption=CaptionTextField.getText();
            loadList();
        }
    }

    /**
     * This method is going to select next photo in the list
     * @param actionEvent
     */
    public void NextPhotoTapped(ActionEvent actionEvent) {
        int index=tableview.getSelectionModel().getSelectedIndex();
        if(index<0){
            tableview.getSelectionModel().select(0);
        }else if(index<currentAlbum.photos.size()-1){
            tableview.getSelectionModel().select(index+1);
        }
    }

    /**
     * This method is going to select last photo in the list
     * @param actionEvent
     */
    public void LastPhotoTapped(ActionEvent actionEvent) {
        int index=tableview.getSelectionModel().getSelectedIndex();
        if(index<=0){
            tableview.getSelectionModel().select(0);
        }else{
            tableview.getSelectionModel().select(index-1);
        }
    }

    /**
     * This method is going to copy the selected photo in to a targeting album
     * @param actionEvent
     */
    public void copyTapped(ActionEvent actionEvent) {
        String targetAlbumName=NewAlbumNameTextField.getText().trim();
        NewAlbumNameTextField.setText("");
        if(!Album.albums.contains(new Album(targetAlbumName))){
            showError("Error!","Target Album does not exist!");
            return;
        }
        int index=tableview.getSelectionModel().getSelectedIndex();
        if(index<0){
            showError("Error!","You should select one photo from Photo List");
            return;
        }
        for(int i=0;i<Album.albums.size();i++){
            if(Album.albums.get(i).name.equals(targetAlbumName)){
                Photo temp=new Photo(currentAlbum.photos.get(index));
                Album.albums.get(i).photos.add(temp);
                break;
            }
        }
    }
    /**
     * This method is going to move the selected photo in to a targeting album
     * @param actionEvent
     */
    public void moveTapped(ActionEvent actionEvent) {
        String targetAlbumName=NewAlbumNameTextField.getText().trim();
        NewAlbumNameTextField.setText("");
        if(!Album.albums.contains(new Album(targetAlbumName))){
            showError("Error!","Target Album does not exist!");
            return;
        }
        int index=tableview.getSelectionModel().getSelectedIndex();
        if(index<0){
            showError("Error!","You should select one photo from Photo List");
            return;
        }
        for(int i=0;i<Album.albums.size();i++){
            if(Album.albums.get(i).name.equals(targetAlbumName)){
                Photo temp=new Photo(currentAlbum.photos.get(index));
                Album.albums.get(i).photos.add(temp);
                break;
            }
        }
        currentAlbum.photos.remove(index);
        loadList();

    }

    /**
     * This method is going to show the selected photo's details, such as tags, in another stage.
     * @param actionEvent
     */
    public void showTapped(ActionEvent actionEvent) {
        if(currentPhotoIndex<0){
            showError("Error!","You should select one photo from Photo List");
            return;
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/PhotoDetail.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle("Photo Details");
        Photos.stage.setScene(new Scene(root, 900, 600));
        Photos.stage.show();

    }
}
