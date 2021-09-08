package app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import structure.Album;
import structure.Photo;
import structure.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the main class of the project
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class Photos extends Application {

    /**
     * This stage represent the current stage that represent on the screen
     */
    public static Stage stage;

    /**
     * This initialize the first page represent on the screen
     * @param primaryStage the first stage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        primaryStage.setTitle("Log in");
        primaryStage.setScene(new Scene(root, 450, 300));
        this.stage=primaryStage;
        primaryStage.show();
    }

    /**
     * This method function is save the date so far
     */
    @Override
    public void stop(){
        try {
            User.writeUsers();
            Album.writeAlbums();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save file
    }


    /**
     * The main method launch the whole project
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}