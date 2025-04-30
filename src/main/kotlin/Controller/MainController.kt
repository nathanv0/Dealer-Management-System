package Controller

import Controller.VehicleTable.buildTable
import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.util.StringConverter
import org.example.groupproject3.Dealer
import org.example.groupproject3.DealerManager
import org.example.groupproject3.PreferencesManager
import org.example.groupproject3.Vehicle
import java.net.URL
import java.util.*

enum class DialogMode { ADD, UPDATE }

class MainController : Initializable {
    // Pane
    @FXML private lateinit var scenePane: AnchorPane
    @FXML private lateinit var imageView: ImageView
    @FXML private lateinit var statusFilterBox: ChoiceBox<String>
    @FXML private lateinit var searchField: TextField

    // Menu bar
    @FXML private lateinit var myLabel: Label
    @FXML private lateinit var dealerChoiceBox: ChoiceBox<String>

    // Action buttons
    @FXML private lateinit var updateButton: Button
    @FXML private lateinit var deleteButton: Button
    @FXML private lateinit var transferButton: Button
    @FXML private lateinit var rentButton: Button
    @FXML private lateinit var returnButton: Button

    // Table view
    @FXML private lateinit var vehicleTable: TableView<Vehicle>
    @FXML private lateinit var typeCol: TableColumn<Vehicle, String>
    @FXML private lateinit var idCol: TableColumn<Vehicle, String>
    @FXML private lateinit var makeCol: TableColumn<Vehicle, String>
    @FXML private lateinit var modelCol: TableColumn<Vehicle, String>
    @FXML private lateinit var priceCol: TableColumn<Vehicle, String>
    @FXML private lateinit var statusCol: TableColumn<Vehicle, String>

    // Model data
    private val dealerManager: DealerManager = DealerManager.getInstance()
    private var dealers: MutableList<Dealer> = mutableListOf()
    private var currentDealer: Dealer? = null
    private var vehiclesOL: ObservableList<Vehicle> = FXCollections.observableArrayList()

    // Auto called when the view is created
    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        // Set default value
        dealerChoiceBox.converter = object : StringConverter<String>() {
            override fun toString(value: String?): String = value ?: "Select Dealer"
            override fun fromString(s: String?): String? = null
        }
        // Handling when user select dealer -> load the table based on selection
        dealerChoiceBox.setOnAction { handleSelectDealer(it) }

        // Build the table
        buildTable(vehicleTable, idCol, typeCol, makeCol, modelCol, priceCol, statusCol)

        // If no vehicle selected then disable update, delete, transfer, rent, return button buttons
        listOf(deleteButton, updateButton, transferButton, rentButton, returnButton).forEach { button ->
            button.disableProperty().bind(vehicleTable.selectionModel.selectedItemProperty().isNull)

        }

        // After the UI is shown, check if the last file exist -> open it
        val lastFile = PreferencesManager.getLastOpenedFile()
        if (lastFile != null) {
            println("Loading the last file: $lastFile")
            loadDealerFile(lastFile)
        } else {
            loadDealerFile("./src/main/resources/Dealer.xml")
        }

        statusFilterBox.items = FXCollections.observableArrayList(
            "All", "Available", "Rented", "Sports Car", "SUV", "Sedan", "Truck"
        )
        statusFilterBox.value = "All"
        statusFilterBox.setOnAction {
            val selStatus = statusFilterBox.value
            vehicleTable.items = when (selStatus) {
                "All" -> vehiclesOL
                "Available", "Rented" -> FXCollections.observableArrayList(
                    vehiclesOL.filter { it.status.equals(selStatus, true) }
                )
                else -> FXCollections.observableArrayList(
                    vehiclesOL.filter { it.type.equals(selStatus, true) }
                )
            }
        }

        searchField.textProperty().addListener { _, _, query ->
            filterVehicles(query)
        }

        vehicleTable.selectionModel.selectedItemProperty().addListener { _, _, v ->
            if (v != null) showVehicleImage(v.type)
        }
    }

    // Load the last opened file when application start
    private fun loadDealerFile(filePath: String?) {
        dealers = dealerManager.loadDealers(filePath)
        updateDealerChoiceBox()
    }

    // Update the dealer choice box with current list of dealers
    private fun updateDealerChoiceBox() {
        // Update the UI with the loaded dealers
        if (dealers.isNotEmpty()) {
            val dealerNames = dealers.map { it.name}
            // Update the choice box
            dealerChoiceBox.items = FXCollections.observableArrayList(dealerNames)
            // Select the first dealer by default
            dealerChoiceBox.value = dealerNames.first()
        }
    }

    private fun showVehicleImage(vehicleType: String) {
        val imageName = when (vehicleType.lowercase()) {
            "suv" -> "images/suv.png"
            "sedan" -> "images/sedan.png"
            "sports car" -> "images/sports_car.png"
            "truck", "pickup" -> "images/truck.png"
            else -> "default.png"
        }
        try {
            val img = Image(javaClass.getResourceAsStream("/$imageName"))
            imageView.image = if (img.isError) null else img
        } catch (e: Exception) {
            println("Failed to load $imageName: ${e.message}")
            imageView.image = null
        }
    }

    // Handle when user select open file
    @FXML
    fun handleOpenFile(event: ActionEvent?) {
        println("Opening the file")
        val filePath = MenuBarController().openFile()

        if (filePath != null) {
            // Save the file path as the last opened file
            PreferencesManager.saveLastOpenedFile(filePath)

            // Reset and reload dealers
            DealerManager.setDealers(null)
            loadDealerFile(filePath)
        }
    }

    // When user click save option from menu
    @FXML
    fun handleSaveFile(event: ActionEvent?) {
        println("Saving the file")
        val saveFilePath = MenuBarController().saveFile(dealers)

        // If the selected file successfully saved -> update the file preference
        if (saveFilePath != null) {
            PreferencesManager.saveLastOpenedFile(saveFilePath)
        }
    }

    // When user select close option from menu
    @FXML
    fun handleCloseApp(event: ActionEvent?) {
        println("Closing the file")
        MenuBarController().exit(scenePane)
    }

    @FXML fun handleAbout(event: ActionEvent?) {
        Alert(Alert.AlertType.INFORMATION).apply {
            title = "About"
            headerText = "Dealership Management System"
            contentText = "This application allows users to manage vehicle inventory and dealer operations."
        }.showAndWait()
    }

    // Handle when user select vehicle from the Choice Box
    private fun handleSelectDealer(event: ActionEvent?) {
        // Get the value from the user choice
        val dealerName = dealerChoiceBox.value ?: return // If there is no dealer, exit the method
        myLabel.text = dealerName // Change the label based on selected dealer

        for (dealer in dealers) {
            // If their name matches -> add the data to the table
            if (dealer.name == dealerName) {
                currentDealer = dealer
                vehiclesOL = FXCollections.observableArrayList(currentDealer!!.vehicles)
                vehicleTable.items = vehiclesOL
                return  // Stop the loop when the dealer is found -> prevent create a new instance of ObservablesList
            }
        }
    }

    // Handle add new dealer
    @FXML
    fun handleAddDealer(event: ActionEvent?) {
        handleDealerDialog(Dealer(), DialogMode.ADD, "Add new Dealer")
    }

    @FXML
    fun handleUpdateDealer(event: ActionEvent?) {
        if (currentDealer == null) {
            showAlertMessage(AlertType.WARNING, "No Dealer", "Please select a dealer to update")
            return
        }
        handleDealerDialog(currentDealer!!, DialogMode.UPDATE, "Update Dealer")
    }

    @FXML
    fun handleDeleteDealer(event: ActionEvent?) {
        if (currentDealer != null) {
            showAlertMessage(
                AlertType.CONFIRMATION,
                "Deleting current dealer",
                "Are you sure you want to delete current dealer?"
            )
            dealers.remove(currentDealer!!)

            // If we delete the last dealer
            if (dealers.isEmpty()) {
                currentDealer = null
                vehiclesOL = FXCollections.observableArrayList()
                vehicleTable.setItems(vehiclesOL)
            } else {
                currentDealer = dealers[0]
            }
            // Update the ChoiceBox
            updateDealerChoiceBox()
        }
    }

    private fun handleDealerDialog(dealer: Dealer, mode: DialogMode, dialogTitle: String?) {
        try {
            // Load the fxml file and create a new popup dialog
            val fxmlLoader = FXMLLoader(javaClass.getResource("/org/example/groupproject3/DealerDialog.fxml"))
            val vehicleDialogPane = fxmlLoader.load<DialogPane>()

            // Get the vehicle controller
            val dealerDialogController = fxmlLoader.getController<DealerDialogController>()
            dealerDialogController.setDealer(dealer)

            // Create a dialog button type
            val dialog = Dialog<ButtonType>()
            dialog.dialogPane = vehicleDialogPane
            dialog.title = dialogTitle

            // Option button to handle event
            val clickedButton = dialog.showAndWait()
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                println("User clicked OK")
                if (mode == DialogMode.ADD) {
                    // Add the new dealer to the list of dealers
                    dealers.add(dealer)
                    println("New dealer added: $dealer")
                }
                // Update the ChoiceBox
                updateDealerChoiceBox()

                // Select the newly added dealer in the ChoiceBox
                dealerChoiceBox.value = dealer.name
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @FXML
    fun handleAddVehicle(event: ActionEvent?) {
        if (currentDealer == null) {
            showAlertMessage(AlertType.WARNING, "No Dealer Selected", "Please select a dealer before adding a vehicle")
        }
        handleVehicleDialog(Vehicle(), DialogMode.ADD, "Add new Vehicle")
    }

    // Handle update vehicle
    @FXML
    fun handleUpdateVehicle(event: ActionEvent?) {
        val vehicle = vehicleTable.selectionModel.selectedItem as Vehicle
        handleVehicleDialog(vehicle, DialogMode.UPDATE, "Update Vehicle")
    }

    private fun handleVehicleDialog(vehicle: Vehicle, mode: DialogMode, dialogTitle: String) {
        try {
            // Load the fxml file and create a new popup dialog
            val fxmlLoader = FXMLLoader(javaClass.getResource("/org/example/groupproject3/VehicleDialog.fxml"))
            val vehicleDialogPane = fxmlLoader.load<DialogPane>()

            // Get the vehicle controller
            val vehicleDialogController = fxmlLoader.getController<VehicleDialogController>()
            vehicleDialogController.setVehicle(vehicle)

            // Create a dialog button type
            val dialog = Dialog<ButtonType>()
            dialog.dialogPane = vehicleDialogPane
            dialog.title = dialogTitle
            // Option button to handle event
            val clickedButton = dialog.showAndWait()
            // When user click OK
            if (clickedButton.get() == ButtonType.OK) {
                println("User clicked OK")
                if (mode == DialogMode.ADD && currentDealer != null) {
                    currentDealer?.vehicles?.add(vehicle)
                    vehiclesOL.add(vehicle)
                    println("Vehicle added: $vehicle")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Handle when user delete vehicle
    @FXML
    fun handleDeleteVehicle(event: ActionEvent?) {
        // Select vehicle from the table
        val selectedVehicle = vehicleTable.selectionModel.selectedItem as Vehicle
        // Show alert dialog before delete vehicle
        showAlertMessage(
            AlertType.CONFIRMATION,
            "Deleting vehicle",
            "Are you sure you want to delete selected vehicle?"
        )
        currentDealer?.vehicles?.remove(selectedVehicle)
        vehiclesOL.remove(selectedVehicle)
        vehicleTable.selectionModel.clearSelection() // Clear the selection
        println("Deleted: $selectedVehicle\n")
    }

    // Handle when user select rent button
    @FXML
    fun handleRentVehicle(event: ActionEvent?) {
        // Select vehicle from the table
        val selectedVehicle = vehicleTable.selectionModel.selectedItem as Vehicle
        // If the vehicle is not sport and not null
        if (!selectedVehicle.type.equals("sports car", ignoreCase = true)) {
            if (selectedVehicle.status.equals("Available", ignoreCase = true)) {
                selectedVehicle.status = "Rented"
                vehicleTable.refresh()
                println("Vehicle rented: $selectedVehicle")
            } else {
                println("This vehicle is not available for rented")
            }
        } else {
            println("Can not rent sport car.")
            showAlertMessage(AlertType.ERROR, "Car type error", "A sport car can not be rented")
        }
    }

    @FXML
    fun handleReturnVehicle(event: ActionEvent?) {
        // Select vehicle from the table
        val selectedVehicle = vehicleTable.selectionModel.selectedItem as Vehicle
        if (selectedVehicle.status.equals("Rented", ignoreCase = true)) {
            selectedVehicle.status = "Available"
            vehicleTable.refresh()
            println("Vehicle returned: $selectedVehicle")
        } else {
            println("This vehicle is not currently rented.")
            showAlertMessage(AlertType.ERROR, "Error", "This vehicle is not currently rented")
        }
    }

    @FXML
    fun handleTransferVehicle(event: ActionEvent?) {
        // Get the selected vehicle
        val selectedVehicle = vehicleTable.selectionModel.selectedItem as Vehicle
        // Create a ChoiceDialog to select the target dealer
        val dealerNames: MutableList<String> = ArrayList()
        for (dealer in dealers) {
            dealerNames.add(dealer.name)
        }

        val dealerDialog = ChoiceDialog(dealerNames.first(), dealerNames)
        dealerDialog.title = "Transfer Vehicle"
        dealerDialog.headerText = "Select the dealer to transfer the vehicle to:"
        dealerDialog.contentText = "Dealer:"

        val result = dealerDialog.showAndWait()
        if (result.isPresent) {
            val targetDealerName = result.get()

            // Find the target dealer by name
            var targetDealer: Dealer? = null
            for (dealer in dealers) {
                if (dealer.name == targetDealerName) {
                    targetDealer = dealer
                    break
                }
            }
            if (targetDealer == null) {
                println("Target dealer not found.")
                return  // Exit if the dealer was not found
            }
            // Remove the vehicle from the current dealer and add it to the target dealer
            if (currentDealer != null) {
                currentDealer!!.vehicles.remove(selectedVehicle)
                targetDealer.vehicles.add(selectedVehicle)

                // Update the observable list and refresh the table
                vehiclesOL.remove(selectedVehicle)
                // selector widget
                dealerChoiceBox.value = targetDealerName

                // Inform the user
                println("Vehicle transferred to: " + targetDealer.name)
            }
        }
    }

    // Utility method use to show Alert Message
    private fun showAlertMessage(alertType: AlertType, title: String, message: String) {
        val alert = Alert(alertType)
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    private fun filterVehicles(query: String) {
        val list = if (query.isBlank()) vehiclesOL
        else vehiclesOL.filter { v ->
            v.id.contains(query, true) ||
                    v.make.contains(query, true) ||
                    v.model.contains(query, true) ||
                    v.type.contains(query, true)
        }
        vehicleTable.items = FXCollections.observableArrayList(list)
    }
}
