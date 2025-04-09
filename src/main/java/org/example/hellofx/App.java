package org.example.hellofx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Load the fxml file to the root
            Parent root = FXMLLoader.load(getClass().getResource("FriendEditor.fxml"));

            // Create a Scene by passing the root
            Scene scene = new Scene(root);

            stage.setTitle("Dealership Management System");
            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Can't open the file");
        }
    }


    public static void main(String[] args) {
        launch();
    }
}