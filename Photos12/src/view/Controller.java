package view;

import app.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import structure.Album;
import structure.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The super class of Controllers
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */

public class Controller {
    /**
     * The method is used to go back to stage in upper level
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
            root = FXMLLoader.load(getClass().getResource("../view/UserDashboard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Photos.stage.setTitle(User.CurrentUsername+"'s Home");
        Photos.stage.setScene(new Scene(root, 900, 600));
        Photos.stage.show();
    }

    /**
     * Thie method is used to log out the current user.
     * @param actionEvent
     */
    public void logoutTapped(ActionEvent actionEvent) {
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
        User.CurrentUsername=null;
        Album.currentAlbumName=null;
        AlbumController.currentPhotoIndex=-1;
        Album.albums=new ArrayList<>();
        Photos.stage.show();
    }

    /**
     * This method is going to show the error Alert
     * @param header The header of the error message
     * @param mess The content of the error message
     */
    public void showError(String header,String mess){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(mess);
        alert.showAndWait();
    }
}
