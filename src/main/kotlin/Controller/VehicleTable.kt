package Controller

import javafx.scene.control.Label
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import org.example.groupproject3.Vehicle

object VehicleTable {
    fun buildTable(
        vehicleTable: TableView<Vehicle>,
        idCol: TableColumn<Vehicle, String>,
        typeCol: TableColumn<Vehicle, String>,
        makeCol: TableColumn<Vehicle, String>,
        modelCol: TableColumn<Vehicle, String>,
        priceCol: TableColumn<Vehicle, String>,
        statusCol: TableColumn<Vehicle, String>
    ) {
        // Set placeholder message
        val noDataLabel = Label("No vehicles in inventory")
        noDataLabel.style = "-fx-text-fill: gray; -fx-font-size: 16px;"
        vehicleTable.placeholder = noDataLabel

        // Set the column to an associate value
        idCol.cellValueFactory = PropertyValueFactory("id")
        typeCol.cellValueFactory = PropertyValueFactory("type")
        makeCol.cellValueFactory = PropertyValueFactory("make")
        modelCol.cellValueFactory = PropertyValueFactory("model")
        priceCol.cellValueFactory = PropertyValueFactory("price")
        statusCol.cellValueFactory = PropertyValueFactory("status")
    }
}
