package Controller

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.example.groupproject3.Dealer
import org.example.groupproject3.JSONFileHandler
import org.example.groupproject3.XMLFileHandler
import java.io.File
import java.util.*

class MenuBarController {
    private var stage: Stage? = null

    // Extension filter: filter .xml or .json file
    private var xmlExtension = FileChooser.ExtensionFilter("XML Files", "*.xml")
    private var jsonExtension = FileChooser.ExtensionFilter("JSON Files", "*.json")
    private var allFile = FileChooser.ExtensionFilter("All Files", "*.*")

    // Method for open file
    fun openFile(): String? {
        // Create a lambda to set all attributes for the files
        val fileChooser = FileChooser().apply {
            extensionFilters.addAll(xmlExtension, jsonExtension, allFile)
            title = "Import xml data" // Set the title of open dialog
            initialDirectory = File("./src/main/resources") // Set the initial path
        }

        val selectedFile = fileChooser.showOpenDialog(stage)

        if (selectedFile != null) {
            println("File selected")
            println(selectedFile.path)
            return selectedFile.path
        } else {
            println("File selection cancelled")
        }
        return null // Return null to prevent error
    }

    // Method for saving the file
    fun saveFile(dealers: List<Dealer?>): String? {
        val fileChooser = FileChooser().apply {
            extensionFilters.addAll(jsonExtension, allFile, xmlExtension)
            title = "Save as" // Set the title of open dialog
            initialDirectory = File("./src/main/resources") // Set the initial path
        }

        val selectedFile = fileChooser.showSaveDialog(stage)

        if (selectedFile != null) {
            val path = selectedFile.path.lowercase(Locale.getDefault())
            println("File selected")

            // Check if the user save as json or xml file
            when {
                path.endsWith(".json") -> {
                    JSONFileHandler().saveDealers(dealers, selectedFile.path)
                }
                path.endsWith(".xml") ->
                    XMLFileHandler().saveDealers(dealers, selectedFile.path)
                else -> {
                    println("Unsupported file format. Please use .json or .xml file.")
                    return null
                }
            }
            return selectedFile.path // Return the saved path
        } else {
            println("File selection cancelled")
            return null // Return null if no file selected
        }
    }

    // Method for exit file
    fun exit(scenePane: AnchorPane) {
        // Create an alert window to ask for confirmation
        val alert = Alert(Alert.AlertType.CONFIRMATION).apply{
            title = "Exit???"
            headerText = "You're about to exit the app!"
            contentText = "Make sure you save before existing?"

        }

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = scenePane.scene.window as Stage
            println("You're successfully exit")
            stage?.close()
        }
    }
}
