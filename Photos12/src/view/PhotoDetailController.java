package view;

import app.Photos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import structure.Album;
import structure.User;

import javax.swing.text.html.HTML;
import java.lang.reflect.Array;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The controller that control the PhotoDetail page
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class PhotoDetailController extends  Controller{
    /**
     * The imageview show the image we choose
     */
    @FXML
    ImageView ImageShow;
    /**
     * This label show the link of the photo
     */
    @FXML
    Label PhotoName;
    /**
     * This label show the caption of the photo
     */
    @ FXML
    Label Caption;
    /**
     * This label show the date of the photo
     */
    @FXML
    Label Date;
    /**
     * This textfield is the place for us to input tag name for delete or add
     */
    @FXML
    TextField TagName;
    /**
     * This textfield is  the place for us to input tag value for delete or add
     */
    @FXML
    TextField TagValue;
    /**
     * This button for us to delete specific tag
     */
    @FXML
    Button DeleteTag;
    /**
     * This button for us to add new tag
     */
    @FXML
    Button NewTag;
    /**
     * This listview show the current tag that existed for this photo
     */
    @FXML
    ListView<String> TagList;
    /**
     * This listview show the suggested tagname
     */
    @FXML
    ListView<String> suggestedTagName;


    /**
     * This method help us to return to the album page
     * @param actionEvent
     */
    public void backTapped(ActionEvent actionEvent) {
        try {
            User.writeUsers();
            Album.writeAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/Album.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle(Album.currentAlbumName);
        Photos.stage.setScene(new Scene(root, 900, 600));
        Photos.stage.show();
    }

    /**
     * This method help us to initialize the photodetail page once we enter it.
     */
    @FXML
    void initialize(){
        PhotoName.setText("Link: "+AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).path);
        Caption.setText("Caption: "+AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).caption);
        TagList. getItems().clear();

        for(Map.Entry<String,ArrayList<String>> temp: AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.entrySet())
        {
            ArrayList<String> a=temp.getValue();
            for(int i=0;i<a.size();i++)
            {
                String temp1=temp.getKey()+":"+a.get(i);
                TagList.getItems().add(temp1);
            }
        }
        SimpleDateFormat sdf=new SimpleDateFormat(("yyyy-MM-dd"));
        Date dateType=AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).date.getTime();
        String dateStr=sdf.format(dateType);
        Date.setText("Date: "+dateStr);
        ImageShow.setImage(AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).getImage());
        suggestedTagName.getItems().clear();
        suggestedTagName.getItems().add("location");
        suggestedTagName.getItems().add("person");
        suggestedTagName.getItems().add("mood");
        for(Map.Entry<String,ArrayList<String>> a:AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.entrySet())
        {
            if(!(suggestedTagName.getItems().contains(a.getKey())))
            {
                suggestedTagName.getItems().add(a.getKey());
            }
        }
        suggestedTagName.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

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
                String str = suggestedTagName.getSelectionModel().getSelectedItem();
                if(str!=null)
                    TagName.setText(String.valueOf(str));
                else
                    TagName.setText("");
            }
        });
    }

    /**
     * This method help us to determine if we need to add new tag name to the suggested tagname listview
     * @param a The new tag name we want to judge
     * @return If the tag name do not exist yet, we need to add it to the listview
     */
    public boolean checkSuggestedList(String a)
    {
        for(int i=0;i<suggestedTagName.getItems().size();i++)
        {
            if(a.equals(suggestedTagName.getItems().get(i)))
                return false;
        }
        return true;
    }
    /**
     * This method help us to add new tag
     * @param actionEvent
     */
    public void NewTagTapped(ActionEvent actionEvent) {
        if(TagName.getText().isEmpty() || TagValue.getText().isEmpty()) {
                showError("Content Cannot Be Empty","Please Try Again");
                TagName.setText("");
                TagValue.setText("");
                return;
        }
        ArrayList<String> temp = new ArrayList<String>();
        if(!(AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.containsKey(TagName.getText().toLowerCase())))
            temp.add(TagValue.getText().toLowerCase());
        else
            temp = AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.get(TagName.getText().toLowerCase());
        if (!(temp.contains(TagValue.getText().toLowerCase()))) {
                temp.add(TagValue.getText().toLowerCase());
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.remove(TagName.getText().toLowerCase());
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.put(TagName.getText().toLowerCase(), temp);
            } else {
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.remove(TagName.getText().toLowerCase());
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.put(TagName.getText().toLowerCase(), temp);

            }
        TagList. getItems().clear();
        for(Map.Entry<String,ArrayList<String>> temp2: AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.entrySet())
        {
            ArrayList<String> a=temp2.getValue();
            for(int i=0;i<a.size();i++)
            {
                String temp1=temp2.getKey()+"="+a.get(i);
                TagList.getItems().add(temp1);
            }
        }
        TagName.setText("");
        TagValue.setText("");
        for(Map.Entry<String,ArrayList<String>> a:AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.entrySet())
        {
            if(!(suggestedTagName.getItems().contains(a.getKey())))
            {
                suggestedTagName.getItems().add(a.getKey());
            }
        }
    }

    /**
     * This method help us to delete tag
     * @param actionEvent
     */
    public void DeleteTagTapped(ActionEvent actionEvent){
        if(TagName.getText().isEmpty() || TagValue.getText().isEmpty())
        {
            showError("Content can not be empty","Please try again");
            TagName.setText("");
            TagValue.setText("");
            return;
        }
        boolean flag=AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.containsKey(TagName.getText().toLowerCase());
        if(flag)
        {
            ArrayList<String> temp=new ArrayList<String>();
            temp=AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.get(TagName.getText().toLowerCase());
            flag=false;
            flag=temp.contains(TagValue.getText().toLowerCase());
            if(flag)
            {
                temp.remove(TagValue.getText().toLowerCase());
                String tempName=TagName.getText().toLowerCase();
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.remove(TagName.getText().toLowerCase());
                AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.put(tempName,temp);
            }
            else if(TagValue.getText().isEmpty())
                showError("Bad input","please input the tag value");
            else
                showError("Bad input","This map of tagname and tagvalue do not exist");
        }
        TagList. getItems().clear();
        for(Map.Entry<String,ArrayList<String>> temp: AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags.entrySet())
        {
            ArrayList<String> a=temp.getValue();
            for(int i=0;i<a.size();i++)
            {
                String temp1=temp.getKey()+":"+a.get(i);
                TagList.getItems().add(temp1);
            }
        }
        TagValue.setText("");
        TagName.setText("");
        //        TagList. getItems().clear();
//        TagList.getItems().addAll(AlbumController.currentAlbum.photos.get(AlbumController.currentPhotoIndex).tags);
    }


    public void LastPhotoTapped(ActionEvent actionEvent) {
        int num=AlbumController.currentAlbum.photos.size();
        int num2=AlbumController.currentPhotoIndex;
        if(num2==0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Already reach first photo");
            alert.setContentText("Already reach first photo");
            alert.showAndWait();
            return;
        }
        if(num2-1>=0)
        {
            AlbumController.currentPhotoIndex--;
        }
        initialize();
    }

    /**
     * This method show the next photo
     * @param actionEvent
     */
    public void NextPhotoTapped(ActionEvent actionEvent) {
            int num=AlbumController.currentAlbum.photos.size();
            int num2=AlbumController.currentPhotoIndex;
            if(num2==num-1)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Already reach to the end");
                alert.setContentText("Already reach to the end of the Album");
                alert.showAndWait();
                return;
            }
            if(num2+1<num)
            {
                AlbumController.currentPhotoIndex++;
            }
            initialize();
    }
}
