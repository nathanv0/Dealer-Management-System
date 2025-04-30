package Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.groupproject3.Dealer;
import org.example.groupproject3.JSONFileHandler;
import org.example.groupproject3.XMLFileHandler;

import java.io.File;
import java.util.List;

public class MenuBarController {
    Stage stage;

    // Extension filter: filter .xml or .json file
    FileChooser.ExtensionFilter xmlExtension = new FileChooser.ExtensionFilter("XML Files", "*.xml");
    FileChooser.ExtensionFilter jsonExtension = new FileChooser.ExtensionFilter("JSON Files", "*.json");
    FileChooser.ExtensionFilter allFile = new FileChooser.ExtensionFilter("All Files", "*.*");

    // Method for open file
    public String openFile() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(xmlExtension, jsonExtension, allFile);

        fileChooser.setTitle("Import xml data"); // Set the title of open dialog
        fileChooser.setInitialDirectory(new File("./src/main/resources")); // Set the initial path

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("File selected");
            System.out.println(selectedFile.getPath());
            return selectedFile.getPath();
        } else {
            System.out.println("File selection cancelled");
        }
        return "./src/main/resources/Dealer.xml"; // Return default dealer.xml to prevent error
    }

    // Method for saving the file
    public String saveFile( List<Dealer> dealers) {
        FileChooser fileChooser = new FileChooser();


        fileChooser.getExtensionFilters().addAll(jsonExtension, allFile, xmlExtension);
        fileChooser.setTitle("Save as"); // Set the title of open dialog
        fileChooser.setInitialDirectory(new File("./src/main/resources")); // Set the initial path
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            String path = selectedFile.getPath().toLowerCase();
            System.out.println("File selected");

            // Check if the user save as json or xml file
            if (path.endsWith(".json")) {
                JSONFileHandler jsonFileHandler = new JSONFileHandler();
                jsonFileHandler.saveDealers(dealers, selectedFile.getPath());
            }
            else if (path.endsWith(".xml")) {
                XMLFileHandler xmlFileHandler = new XMLFileHandler();
                xmlFileHandler.saveDealers(dealers, selectedFile.getPath());
            } else {
                System.out.println("Unsupported file format. Please use .json or .xml file.");
                return null;
            }
            return selectedFile.getPath(); // Return the saved path
        } else {
            System.out.println("File selection cancelled");
        }
        return null; // Return null if no file selected
    }


    // Method for exit file
    public void exit(AnchorPane scenePane) {
        // Create an alert window to ask for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit???");
        alert.setHeaderText("You're about to exit the app!");
        alert.setContentText("Make sure you save before existing?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            System.out.println("You're successfully exit");
            stage.close();
        }
    }
}
