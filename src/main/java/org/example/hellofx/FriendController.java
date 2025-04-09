package org.example.hellofx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class FriendController implements Initializable {

    Friend myFriend;
    @FXML
    private TextField ageField;

    @FXML
    private TextField nameField;

    @FXML
    private Label label;

    @FXML
    void handleSubmit(ActionEvent event) {
        //myFriend.setName(nameField.getText());
        //myFriend.setAge( Integer.valueOf(ageField.getText()) );
        System.out.println(myFriend);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myFriend = new Friend("Jonn", 23);
        System.out.println(myFriend);

        // Show the detail of myFriend in the form
        nameField.textProperty().bindBidirectional(myFriend.nameProperty());
        ageField.textProperty().bindBidirectional(myFriend.ageProperty(), NumberFormat.getNumberInstance());

        label.textProperty().bind(myFriend.nameProperty().concat(myFriend.ageProperty()));
//        nameField.setText(myFriend.getName());
//        ageField.setText( String.valueOf(myFriend.getAge()) );
    }
}