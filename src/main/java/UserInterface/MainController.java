package UserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.example.hellofx.Dealer;
import org.example.hellofx.ReadXMLFile;
import org.example.hellofx.Vehicle;


import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private AnchorPane scenePane;
    @FXML private Label outputLabel;

    @FXML private Label myLabel;
    @FXML private ChoiceBox<String> dealerChoiceBox;

    @FXML private TableView vehicleTable;
    @FXML private TableColumn<Vehicle, String> typeCol;
    @FXML private TableColumn<Vehicle, String> idCol;
    @FXML private TableColumn<Vehicle, String> makeCol;
    @FXML private TableColumn<Vehicle, String> modelCol;
    @FXML private TableColumn<Vehicle, String> priceCol;
    @FXML private TableColumn<Vehicle, String> statusCol;

    List<Dealer> dealers;
    private Dealer currentDealer;
    String filePath = "";

    ObservableList<Vehicle> dealersOL = null;

    // Auto called when the view is created
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dealers = ReadXMLFile.getDealers();

        List<String> namesList = new ArrayList<>();
        for (Dealer dealer : dealers) {
            namesList.add(dealer.getName());

        }
        ObservableList<String> dealerNameOL = FXCollections.observableArrayList(namesList);
        // Choice box switch dealer: Adding all dealer name to the choiceList
        dealerChoiceBox.setItems(dealerNameOL);

        // Set default value
        dealerChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String s) {
                return (s == null) ? "Select Dealer" : s;
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });
        // Handling when user select dealer -> load the table based on selection
        dealerChoiceBox.setOnAction(this::handleSelectDealer);

        // Build the table
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol);

        // Handle adding vehicle
//        addVehicleButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                Dialog addVehicleDialog = new AddVehicleDialog();
//                addVehicleDialog.setTitle("Add Vehicle");
//                Optional<Vehicle> result = addVehicleDialog.showAndWait();
//
//            }
//        });

    }

    // Handle when user select open file
    @FXML
    void handleOpenFile(ActionEvent event) {
        System.out.println("Opening the file");
        new MenuBarController().openFile(outputLabel);
    }

    // When user click save option from menu
    @FXML
    void handleSaveFile(ActionEvent event) {
        System.out.println("Saving the file");
        new MenuBarController().saveFile(outputLabel);
    }

    // When user select close option from menu
    @FXML
    void handleCloseApp(ActionEvent event) {
        System.out.println("Closing the file");
        new MenuBarController().exit(scenePane);
    }

    // Handle when user select vehicle from the Choice Box
    void handleSelectDealer(ActionEvent event) {
        // Get the value from the user choice
        String dealerName = dealerChoiceBox.getValue(); // get dealer name
        myLabel.setText(dealerName); // Change the label based on selected dealer

        // Loop through each dealer
        for (int i = 0; i < dealers.size(); i++) {
            if (!dealers.isEmpty()) {
                currentDealer = dealers.get(i);
                dealersOL = FXCollections.observableArrayList(currentDealer.getVehicles());
                // If their name matches -> add the data to the table
                if (currentDealer.getName().equals(dealerName)) {
                    vehicleTable.setItems(dealersOL);
                    return; // Stop the loop when the dealer is found -> prevent create a new instance of ObservablesList
                }
            }
        }
    }

    // Handle when user delete vehicle
    @FXML
    void handleDeleteVehicle(ActionEvent event) {
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            currentDealer.getVehicles().remove(selectedVehicle);
            dealersOL.remove(selectedVehicle);
            // Clear the selection
            vehicleTable.getSelectionModel().clearSelection();
            System.out.printf("Deleted: %s\n", selectedVehicle);
        } else {
            System.out.println("No vehicle selected to delete.");
        }
    }
}
