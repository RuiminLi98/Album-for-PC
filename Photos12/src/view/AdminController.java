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
import structure.Admin;
import structure.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
/**
 * The controller that controls the Admin page
 * @author Junjie He (jh1285)
 * @author Ruimin Li (rl751)
 */
public class AdminController extends Controller{

    /**
     * The textField contains the new username to be added
     */
    @FXML
    TextField AddUserTextFeild;

    /**
     * The textField contains the existing username to be deleted
     */
    @FXML
    TextField DeleteUserTextField;

    /**
     * The List to show the current users
     */
    @FXML
    ListView<String> UsersList;

    /**
     * This method is used to load users from ArrayList into list view
     */
    public void loadList(){
        ObservableList observableList = FXCollections.observableArrayList ();
        if(User.users!=null){
            Collections.sort(User.users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.username.compareTo(o2.username);
                }
            });
            for(User u:User.users){
                observableList.add(u.username);
            }
            UsersList.setItems(observableList);
        }
    }

    /**
     * The method is going to delete a existing user
     * @param actionEvent
     */
    public void DeleteTapped(ActionEvent actionEvent) {
        if(DeleteUserTextField.getText().toLowerCase().equals("stock")){
            showError("Warning","Stock cannot be deleted! However, All contents in the stock have been reset!");
        }
        if(!Admin.deleteUser(DeleteUserTextField.getText().toLowerCase())){
            showError("Error","Illegal Username");
        }
        DeleteUserTextField.setText("");
        loadList();
    }

    /**
     * The method is going to add a ne user
     * @param actionEvent
     */
    public void AddTapped(ActionEvent actionEvent) {
        if(!Admin.createUser(AddUserTextFeild.getText().toLowerCase())){
            showError("Error","Illegal Username");
        }
        AddUserTextFeild.setText("");
        loadList();
    }

    /**
     * The initialize method should be run at the beginning of the stage
     */
    @FXML
    void initialize(){
        loadList();
    }


}
