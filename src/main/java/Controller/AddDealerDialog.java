package Controller;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.hellofx.Dealer;

public class AddDealerDialog extends Dialog<Dealer> {

    private TextField idField;
    private TextField nameField;

    public AddDealerDialog() {
        setTitle("Add New Dealer");
        setHeaderText("Enter dealer details");

        // Create fields for dealer details
        idField = new TextField();
        nameField = new TextField();

        // Create the layout
        GridPane grid = new GridPane();
        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        getDialogPane().setContent(grid);

        // Add buttons to dialog
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Set result converter for dialog
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String id = idField.getText();
                String name = nameField.getText();

                // Check if all fields are filled
                if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                    showErrorMessage("Both ID and Name are required!");
                    return null;
                }

                // Create and return a new Dealer with the provided values
                return new Dealer(id, name);
            }
            return null;
        });
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
