package Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import org.example.groupproject3.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

enum DialogMode {ADD, UPDATE}

public class MainController implements Initializable {
    // Pane
    @FXML private AnchorPane scenePane;

    // Menu bar
    @FXML private Label myLabel;
    @FXML private ChoiceBox<String> dealerChoiceBox;
    // Action buttons
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button transferButton;
    @FXML private Button rentButton;
    @FXML private Button returnButton;
    // Table view
    @FXML private TableView vehicleTable;
    @FXML private TableColumn<Vehicle, String> typeCol;
    @FXML private TableColumn<Vehicle, String> idCol;
    @FXML private TableColumn<Vehicle, String> makeCol;
    @FXML private TableColumn<Vehicle, String> modelCol;
    @FXML private TableColumn<Vehicle, String> priceCol;
    @FXML private TableColumn<Vehicle, String> statusCol;

    // Model data
    private final DealerManager dealerManager = DealerManager.getInstance();
    private List<Dealer> dealers;
    private Dealer currentDealer;
    private ObservableList<Vehicle> vehiclesOL;

    // Auto called when the view is created
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default value
        dealerChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String s) { return (s == null) ? "Select Dealer" : s; }

            @Override
            public String fromString(String s) { return null; }
        });
        // Handling when user select dealer -> load the table based on selection
        dealerChoiceBox.setOnAction(this::handleSelectDealer);

        // Build the table
        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol);

        // If no vehicle selected then disable update, delete, transfer, rent, return button buttons
        deleteButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        updateButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        transferButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        rentButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        returnButton.disableProperty().bind( Bindings.isNull(
                vehicleTable.getSelectionModel().selectedItemProperty()) );

        // After the UI is shown, check if the last file exist -> open it
        String lastFile = PreferencesManager.getLastOpenedFile();
        if (lastFile != null) {
            System.out.printf("Loading the last file: " + lastFile);
            loadDealerFile(lastFile);
        } else {
            loadDealerFile("./src/main/resources/Dealer.xml");
        }
    }

    // Load the last opened file when application start
    public void loadDealerFile(String filePath) {
        dealers = dealerManager.loadDealers(filePath);
        updateDealerChoiceBox();
    }

    // Update the dealer choice box with current list of dealers
    private void updateDealerChoiceBox() {
        // Update the UI with the loaded dealers
        if (dealers != null && !dealers.isEmpty()) {
            List<String> dealerNames = getDealerNames();
            // Update the choice box
            ObservableList<String> dealerNameOL = FXCollections.observableArrayList(dealerNames);
            dealerChoiceBox.setItems(dealerNameOL);
            // Select the first dealer by default
            dealerChoiceBox.setValue(dealerNames.getFirst());
        }
    }

    // Get the list of dealer names
    private List<String> getDealerNames() {
        // Create a list of dealer names
        List<String> namesList = new ArrayList<>();
        for (Dealer dealer : dealers) {
            namesList.add(dealer.getName());
        }
        return namesList;
    }

    // Handle when user select open file
    @FXML
    void handleOpenFile(ActionEvent event) {
        System.out.println("Opening the file");
        String filePath = new MenuBarController().openFile();

        if (filePath != null) {
            // Save the file path as the last opened file
            PreferencesManager.saveLastOpenedFile(filePath);

            // Reset and reload dealers
            DealerManager.setDealers(null);
            loadDealerFile(filePath);
        }
    }

    // When user click save option from menu
    @FXML
    void handleSaveFile(ActionEvent event) {
        System.out.println("Saving the file");
        String saveFilePath = new MenuBarController().saveFile(dealers);

        // If the selected file successfully saved -> update the file preference
        if (saveFilePath != null) {
            PreferencesManager.saveLastOpenedFile(saveFilePath);
        }
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
        // If there is no dealer, exit the method
        if (dealerName == null) return;
        myLabel.setText(dealerName); // Change the label based on selected dealer

        for (Dealer dealer : dealers) {
            // If their name matches -> add the data to the table
            if (dealer.getName().equals(dealerName)) {
                currentDealer = dealer;
                vehiclesOL = FXCollections.observableArrayList(currentDealer.getVehicles());
                vehicleTable.setItems(vehiclesOL);
                return; // Stop the loop when the dealer is found -> prevent create a new instance of ObservablesList
            }
        }
    }

    // Handle add new dealer
    @FXML
    public void handleAddDealer(ActionEvent event) {
        handleDealerDialog(new Dealer(), DialogMode.ADD, "Add new Dealer");
    }

    @FXML
    public void handleUpdateDealer(ActionEvent event) {
        if (currentDealer == null) {
            showAlertMessage(Alert.AlertType.WARNING, "No Dealer", "Please select a dealer to update");
            return;
        }
        handleDealerDialog(currentDealer, DialogMode.UPDATE, "Update Dealer");
    }

    @FXML
    public void handleDeleteDealer(ActionEvent event) {
        if (currentDealer != null) {
            showAlertMessage(Alert.AlertType.CONFIRMATION, "Deleting current dealer", "Are you sure you want to delete current dealer?");
            dealers.remove(currentDealer);

            // If we delete the last dealer
            if (dealers.isEmpty()) {
                currentDealer = null;
                vehiclesOL = FXCollections.observableArrayList();
                vehicleTable.setItems(vehiclesOL);
            } else {
                currentDealer = dealers.get(0);
            }
            // Update the ChoiceBox
            updateDealerChoiceBox();
        }
    }

    public void handleDealerDialog(Dealer dealer, DialogMode mode, String dialogTitle) {
        try {
            // Load the fxml file and create a new popup dialog
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/example/groupproject3/DealerDialog.fxml"));
            DialogPane vehicleDialogPane = fxmlLoader.load();

            // Get the vehicle controller
            DealerDialogController dealerDialogController = fxmlLoader.getController();
            dealerDialogController.setDealer(dealer);

            // Create a dialog button type
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(vehicleDialogPane);
            dialog.setTitle(dialogTitle);

            // Option button to handle event
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                System.out.println("User clicked OK");
                if (mode == DialogMode.ADD) {
                    // If the user enter all information and there is no dealer yet
                    if (dealers != null) {
                        // Add the new dealer to the list of dealers
                        dealers.add(dealer);
                        System.out.println("New dealer added: " + dealer);
                    }
                }
                // Update the ChoiceBox
                updateDealerChoiceBox();

                // Select the newly added dealer in the ChoiceBox
                dealerChoiceBox.setValue(dealer.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddVehicle(ActionEvent event) {
        if (currentDealer == null) {
            showAlertMessage(Alert.AlertType.WARNING, "No Dealer Selected", "Please select a dealer before adding a vehicle");
        }
        handleVehicleDialog(new Vehicle(), DialogMode.ADD, "Add new Vehicle");
    }

    // Handle update vehicle
    @FXML
    void handleUpdateVehicle(ActionEvent event) {
        Vehicle vehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (vehicle == null) {
            return;
        }
        handleVehicleDialog(vehicle, DialogMode.UPDATE, "Update Vehicle");

    }

    private void handleVehicleDialog(Vehicle vehicle, DialogMode mode, String dialogTitle) {
        try {
            // Load the fxml file and create a new popup dialog
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/org/example/groupproject3/VehicleDialog.fxml"));
            DialogPane vehicleDialogPane = fxmlLoader.load();

            // Get the vehicle controller
            VehicleDialogController vehicleDialogController = fxmlLoader.getController();
            vehicleDialogController.setVehicle(vehicle);

            // Create a dialog button type
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(vehicleDialogPane);
            dialog.setTitle(dialogTitle);
            // Option button to handle event
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                System.out.println("User clicked OK");
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer.getVehicles().add(vehicle);
                    vehiclesOL.add(vehicle);
                    System.out.println("Vehicle added: " + vehicle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle when user delete vehicle
    @FXML
    void handleDeleteVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        // Show alert dialog before delete vehicle
        showAlertMessage(Alert.AlertType.CONFIRMATION, "Deleting vehicle", "Are you sure you want to delete selected vehicle?");
        currentDealer.getVehicles().remove(selectedVehicle);
        vehiclesOL.remove(selectedVehicle);
        vehicleTable.getSelectionModel().clearSelection(); // Clear the selection
        System.out.printf("Deleted: %s\n", selectedVehicle);
    }

    // Handle when user select rent button
    @FXML
    void handleRentVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        // If the vehicle is not sport and not null
        if (selectedVehicle != null && !selectedVehicle.getType().equalsIgnoreCase("sports car")) {
            if (selectedVehicle.getStatus().equalsIgnoreCase("Available")) {
                selectedVehicle.setStatus("Rented");
                vehicleTable.refresh();
                System.out.println("Vehicle rented: " + selectedVehicle);
            } else {
                System.out.println("This vehicle is not available for rented");
            }
        } else {
            System.out.println("Can not rent sport car.");
            showAlertMessage(Alert.AlertType.ERROR, "Car type error", "A sport car can not be rented");
        }

    }

    @FXML
    void handleReturnVehicle(ActionEvent event) {
        // Select vehicle from the table
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            if (selectedVehicle.getStatus().equalsIgnoreCase("Rented")) {
                selectedVehicle.setStatus("Available");
                vehicleTable.refresh();
                System.out.println("Vehicle returned: " + selectedVehicle);
            } else {
                System.out.println("This vehicle is not currently rented.");
                showAlertMessage(Alert.AlertType.ERROR,"Error", "This vehicle is not currently rented");
            }
        } else {
            System.out.println("No vehicle selected to return.");
        }
    }

    @FXML
    void handleTransferVehicle(ActionEvent event) {
        // Get the selected vehicle
        Vehicle selectedVehicle = (Vehicle) vehicleTable.getSelectionModel().getSelectedItem();
        // Create a ChoiceDialog to select the target dealer
        List<String> dealerNames = new ArrayList<>();
        for (Dealer dealer : dealers) {
            dealerNames.add(dealer.getName());
        }

        ChoiceDialog<String> dealerDialog = new ChoiceDialog<>(dealerNames.get(0), dealerNames);
        dealerDialog.setTitle("Select Target Dealer");
        dealerDialog.setHeaderText("Select the dealer to transfer the vehicle to:");
        dealerDialog.setContentText("Dealer:");

        Optional<String> result = dealerDialog.showAndWait();

        if (result.isPresent()) {
            String targetDealerName = result.get();

            // Find the target dealer by name
            Dealer targetDealer = null;
            for (Dealer dealer : dealers) {
                if (dealer.getName().equals(targetDealerName)) {
                    targetDealer = dealer;
                    break;
                }
            }
            if (targetDealer == null) {
                System.out.println("Target dealer not found.");
                return; // Exit if the dealer was not found
            }
            // Remove the vehicle from the current dealer and add it to the target dealer
            if (currentDealer != null) {
                currentDealer.getVehicles().remove(selectedVehicle);
                targetDealer.getVehicles().add(selectedVehicle);

                // Update the observable list and refresh the table
                vehiclesOL.remove(selectedVehicle);
                // selector widget
                dealerChoiceBox.setValue(targetDealerName);

                // Inform the user
                System.out.println("Vehicle transferred to: " + targetDealer.getName());
            }
        }
    }

    private void showAlertMessage(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
