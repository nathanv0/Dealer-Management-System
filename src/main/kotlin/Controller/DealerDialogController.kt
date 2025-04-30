package Controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import org.example.groupproject3.Dealer

class DealerDialogController {
    @FXML
    private lateinit var idField: TextField

    @FXML
    private lateinit var nameField: TextField

    @FXML
    private lateinit var dialogPane: DialogPane

    // Called by main controller to pass a dealer object to a dialog
    fun setDealer(dealer: Dealer) {
        // Bind the dealer property to dialog text field
        idField.textProperty().bindBidirectional(dealer.idProperty())
        nameField.textProperty().bindBidirectional(dealer.nameProperty())

        // Validate
        val button = dialogPane.lookupButton(ButtonType.OK) as Button
        button.addEventFilter(ActionEvent.ACTION) { event: ActionEvent ->
            if (!validateData()) {
                // Validation failed -> prevent the dialog to close
                event.consume()
            }
        }
    }

    // Validate data if the field is null of empty -> If yes, add cursor focus on that field
    private fun validateData(): Boolean {
        if (idField.text.isNullOrEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please enter dealer Id")
            idField.requestFocus()
            return false
        }

        if (nameField.text.isNullOrEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please enter dealer name")
            nameField.requestFocus()
            return false
        }
        return true
    }

    // Show alert dialog
    private fun showAlert(alertType: Alert.AlertType, title: String, message: String) {
        // Create alert object
        val alert = Alert(alertType)
        // Set all attribute and show
        alert.title = title
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}
