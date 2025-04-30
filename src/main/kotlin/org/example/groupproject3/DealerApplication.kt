package org.example.groupproject3

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Stage
import java.io.IOException

class DealerApplication : Application() {
    @Throws(IOException::class)
    override fun start(stage: Stage) {
        try {
            // Load the fxml file to the root
            val loader = FXMLLoader(javaClass.getResource("MainPage.fxml"))
            val root = loader.load<javafx.scene.Parent>()

            // Create a Scene by passing the root
            val scene = Scene(root)

            stage.title = "Dealership Management System" // Set the title of the stage
            // Set the icon of the stage
            stage.icons.add(Image("icon.jpg"))
            // Add the scene to the stage
            stage.scene = scene
            stage.show() // show the stage

            stage.setOnCloseRequest { event ->
                event.consume()
                exitProgram(stage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Can't open the file")
        }
    }

    // Ask for confirmation when we try to exit the program
    fun exitProgram(stage: Stage) {
        // Create an alert window to ask for confirmation
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Exit???"
        alert.headerText = "You're about to exit the app!"
        alert.contentText = "Make sure you save before existing?"

        if (alert.showAndWait().get() == ButtonType.OK) {
            println("You're successfully logout")
            stage.close()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(DealerApplication::class.java)
        }
    }
}
