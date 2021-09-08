package view;


import app.Photos;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import structure.Album;
import structure.Photo;
import structure.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The login controller
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class LoginController extends Controller{

    /**
     * The textfield of username
     */
    @FXML
    TextField textField;

    /**
     * login with the username in the textField
     * @param actionEvent
     * @throws IOException
     */
    public void LoginTapped(ActionEvent actionEvent) throws IOException {
        String user=textField.getText().trim().toLowerCase();
        if(user.isEmpty())return;
        if(user.equals("admin")){
            Parent root = FXMLLoader.load(getClass().getResource("../view/Admin.fxml"));
            Photos.stage.setTitle("Admin");
            Photos.stage.setScene(new Scene(root, 600, 400));
            Photos.stage.show();
        }
        else{
            if(User.users.contains(new User(user))){
                User.CurrentUsername=user;
                Parent root = FXMLLoader.load(getClass().getResource("../view/UserDashboard.fxml"));
                Photos.stage.setTitle(user+"'s Home");
                Photos.stage.setScene(new Scene(root, 900, 600));
                Photos.stage.show();
            }else{
                textField.setText("");
                showError("Wrong Username","The Username does not exist!");
            }
        }

    }

    /**
     * initialize the whole program by reading existing users and load users into memory
     */
    @FXML
    void initialize(){
        try {
            User.users=new ArrayList<>();
            User.readUsers();
            if(!User.users.contains(new User("stock"))){
                User.users.add(new User("stock"));
            }
        } catch (Exception e) {
            User.users.add(new User("stock"));
        }
    }


}
