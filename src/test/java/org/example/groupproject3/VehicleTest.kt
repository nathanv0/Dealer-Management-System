package org.example.groupproject3

import javafx.beans.property.SimpleStringProperty
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VehicleTest {
    private lateinit var vehicle: Vehicle

    @BeforeEach
    fun setUp() {
        vehicle = Vehicle("suv", "V-001", "$30000", "Toyota", "RAV4")
    }

    @Test
    fun testVehicleConstructors() {
        // Test default constructor
        val defaultVehicle = Vehicle()
        assertEquals("", defaultVehicle.id)
        assertEquals("", defaultVehicle.type)
        assertEquals("", defaultVehicle.make)
        assertEquals("", defaultVehicle.model)
        assertEquals("", defaultVehicle.price)
        assertEquals("Available", defaultVehicle.status)

        // Test 5-parameter constructor (the one used in setUp)
        assertEquals("V-001", vehicle.id)
        assertEquals("suv", vehicle.type)
        assertEquals("Toyota", vehicle.make)
        assertEquals("RAV4", vehicle.model)
        assertEquals("$30000", vehicle.price)
        assertEquals("Available", vehicle.status)

        // Test property constructor
        val idProp = SimpleStringProperty("V-002")
        val typeProp = SimpleStringProperty("sedan")
        val modelProp = SimpleStringProperty("Camry")
        val makeProp = SimpleStringProperty("Toyota")
        val priceProp = SimpleStringProperty("$25000")
        val status = "Rented"

        val propVehicle = Vehicle(idProp, typeProp, modelProp, makeProp, priceProp, status)

        assertEquals("V-002", propVehicle.id)
        assertEquals("sedan", propVehicle.type)
        assertEquals("Camry", propVehicle.model)
        assertEquals("Toyota", propVehicle.make)
        assertEquals("$25000", propVehicle.price)
        assertEquals("Rented", propVehicle.status)
    }

    @Test
    fun testPropertyBindings() {
        // Test property bindings
        val idProperty = vehicle.idProperty()
        val typeProperty = vehicle.typeProperty()
        val makeProperty = vehicle.makeProperty()
        val modelProperty = vehicle.modelProperty()
        val priceProperty = vehicle.priceProperty()

        // Test initial values
        assertEquals("V-001", idProperty.get())
        assertEquals("suv", typeProperty.get())
        assertEquals("Toyota", makeProperty.get())
        assertEquals("RAV4", modelProperty.get())
        assertEquals("$30000", priceProperty.get())

        // Test changing values through properties
        idProperty.set("V-NEW")
        typeProperty.set("truck")
        makeProperty.set("Ford")
        modelProperty.set("F-150")
        priceProperty.set("$45000")

        assertEquals("V-NEW", vehicle.id)
        assertEquals("truck", vehicle.type)
        assertEquals("Ford", vehicle.make)
        assertEquals("F-150", vehicle.model)
        assertEquals("$45000", vehicle.price)
    }

    @Test
    fun testStatus() {
        // Test initial status
        assertEquals("Available", vehicle.status)

        // Test setting status
        vehicle.status = "Rented"
        assertEquals("Rented", vehicle.status)

        // Test setting back to Available
        vehicle.status = "Available"
        assertEquals("Available", vehicle.status)
    }

    @Test
    fun testToString() {
        // Test toString output
        val output = vehicle.toString()

        assertTrue(output.contains("id=V-001"))
        assertTrue(output.contains("type=suv"))
        assertTrue(output.contains("make=Toyota"))
        assertTrue(output.contains("model=RAV4"))
        assertTrue(output.contains("price=$30000"))
    }
}