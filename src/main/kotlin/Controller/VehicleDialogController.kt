package Controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import org.example.groupproject3.Vehicle

class VehicleDialogController {
    @FXML
    private lateinit var dialogPane: DialogPane

    @FXML
    private lateinit var idField: TextField

    @FXML
    private lateinit var makeField: TextField

    @FXML
    private lateinit var modelField: TextField

    @FXML
    private lateinit var priceField: TextField

    @FXML
    private lateinit var typeField: TextField

    // Called by the Main controller to pass a vehicle object to a dialog
    fun setVehicle(vehicle: Vehicle) {
        // Bind the vehicle property to the dialog text field
        idField.textProperty().bindBidirectional(vehicle.idProperty())
        typeField.textProperty().bindBidirectional(vehicle.typeProperty())
        makeField.textProperty().bindBidirectional(vehicle.makeProperty())
        modelField.textProperty().bindBidirectional(vehicle.modelProperty())
        priceField.textProperty().bindBidirectional(vehicle.priceProperty())

        // Validate
        val button = dialogPane.lookupButton(ButtonType.OK) as Button
        button.addEventFilter( ActionEvent.ACTION ) { event: ActionEvent ->
            if (!validateData()) {
                // Validation failed -> prevent the dialog to close
                event.consume()
            }
        }
    }

    // Validate data if the field is empty -> show alert and set the cursor focus on that field
    private fun validateData(): Boolean {
        if (idField.text.isNullOrEmpty()) {
            showAlert("Please enter vehicle Id")
            idField.requestFocus()
            return false
        }

        if (typeField.text.isNullOrEmpty()) {
            showAlert("Please enter vehicle type")
            typeField.requestFocus()
            return false
        }

        if (makeField.text.isNullOrEmpty()) {
            showAlert("Please enter vehicle make")
            makeField.requestFocus()
            return false
        }

        if (modelField.text.isNullOrEmpty()) {
            showAlert("Please enter vehicle model")
            modelField.requestFocus()
            return false
        }

        if (priceField.text.isNullOrEmpty()) {
            showAlert("Please enter vehicle price")
            priceField.requestFocus()
            return false
        }
        return true
    }

    // Show alert dialog
    private fun showAlert(message: String) {
        // Create alert object
        val alert = Alert(AlertType.ERROR)
        // Set all attribute and show
        alert.title = "Form Error!"
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}
