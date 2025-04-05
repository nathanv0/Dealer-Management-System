package UserInterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.hellofx.Vehicle;

public class TestApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Create Vbox to store all the component
        VBox root = new VBox();
        // Create a Label and initialize to hello world
        Label label = new Label("Hello World");
        Button button = new Button("Random Color");
        // Add all component to the root
        root.getChildren().addAll(label, button);
        // Set the space between component
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        // Handle click event
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setTextFill(Color.RED);
            }
        });


        // Create a Scene by passing label object, height, and width
        Scene scene = new Scene(root, 400, 300);

        // Add the scene to the stage
        stage.setScene(scene);
        // Set the title of the stage
        stage.setTitle("Testing Only");
        // Display the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
