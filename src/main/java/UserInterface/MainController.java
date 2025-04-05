package UserInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.hellofx.Dealer;
import org.example.hellofx.ReadXMLFile;
import org.example.hellofx.Vehicle;


import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML private AnchorPane scenePane;
    @FXML private MenuItem openFile;
    @FXML private MenuItem closeApp;
    @FXML private MenuItem saveFile;
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
    @FXML private ListView listView;

    @FXML private Button addVehicleButton;
    @FXML private Button deleteVehicleButton;


    private List<Dealer> dealers;
    private Dealer currentDealer;
    private ReadXMLFile readXMLFile = new ReadXMLFile();
    String filePath = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dealers = readXMLFile.getDealers();
        List<String> namesList = new ArrayList<>();
        for (Dealer dealer : dealers) {
            namesList.add(dealer.getName());

        }
        // Choice box switch dealer: Adding all dealer name to the choiceList
        dealerChoiceBox.getItems().addAll(namesList);
        // Handling choiceBox action
        dealerChoiceBox.setOnAction(this::getDealerName);
        // Set default value
        dealerChoiceBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return (s == null) ? "Select Dealer" : s;
            }

            @Override
            public String fromString(String s) {
                return null;
            }
        });


        VehicleTable.buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol);




        // Handle adding vehicle
        addVehicleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Dialog addVehicleDialog = new AddVehicleDialog();
                addVehicleDialog.setTitle("Add Vehicle");
                Optional<Vehicle> result = addVehicleDialog.showAndWait();

            }
        });

        Vehicle vehicle1 = new Vehicle("sedan", "123", "20000", "Toyota", "Camry");
        Vehicle vehicle2 = new Vehicle("sdf", "sdf", "sdf", "sadfsdfd", "Camsdfsdary");
        List<Vehicle> list = new ArrayList<>();
        list.add(vehicle1);
        list.add(vehicle2);
        //vehicleTable.getItems().addAll(list);

        if (!dealers.isEmpty()) {
            currentDealer = dealers.get(0);

            vehicleTable.getItems().setAll(currentDealer.getVehicles());


        }

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



    public void getDealerName(ActionEvent event) {
        // Get the value from the user choice
        String dealerName = dealerChoiceBox.getValue();
        myLabel.setText(dealerName); // Change the name base on the selected choice
    }


}
