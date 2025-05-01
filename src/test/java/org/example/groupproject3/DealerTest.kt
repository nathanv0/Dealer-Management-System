package org.example.groupproject3

import javafx.beans.property.SimpleStringProperty
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DealerTest {
    private lateinit var dealer: Dealer

    @BeforeEach
    fun setUp() {
        dealer = Dealer("D-001", "Test Dealer")
    }

    @Test
    fun testDealerConstructors() {
        // Test default constructor
        val defaultDealer = Dealer()
        assertEquals("", defaultDealer.id)
        assertEquals("", defaultDealer.name)
        assertTrue(defaultDealer.vehicles.isEmpty())

        // Test string constructor
        assertEquals("D-001", dealer.id)
        assertEquals("Test Dealer", dealer.name)
        assertTrue(dealer.vehicles.isEmpty())

        // Test property constructor
        val idProp = SimpleStringProperty("D-002")
        val nameProp = SimpleStringProperty("Property Dealer")
        val propDealer = Dealer(idProp, nameProp)

        assertEquals("D-002", propDealer.id)
        assertEquals("Property Dealer", propDealer.name)
        assertTrue(propDealer.vehicles.isEmpty())
    }

    @Test
    fun testPropertyBindings() {
        // Test property bindings
        val idProperty = dealer.idProperty()
        val nameProperty = dealer.nameProperty()

        // Test initial values
        assertEquals("D-001", idProperty.get())
        assertEquals("Test Dealer", nameProperty.get())

        // Test changing values through properties
        idProperty.set("D-NEW")
        nameProperty.set("New Name")

        assertEquals("D-NEW", dealer.id)
        assertEquals("New Name", dealer.name)

        // Test changing values through setters
        dealer.id = "D-SET"
        dealer.name = "Set Name"

        assertEquals("D-SET", idProperty.get())
        assertEquals("Set Name", nameProperty.get())
    }

    @Test
    fun testVehicleManagement() {
        // Test adding vehicles
        val vehicle1 = Vehicle("sedan", "V-001", "$20000", "Toyota", "Camry")
        val vehicle2 = Vehicle("suv", "V-002", "$30000", "Honda", "CR-V")

        dealer.vehicles.add(vehicle1)
        dealer.vehicles.add(vehicle2)

        assertEquals(2, dealer.vehicles.size)
        assertTrue(dealer.vehicles.contains(vehicle1))
        assertTrue(dealer.vehicles.contains(vehicle2))

        // Test removing vehicles
        dealer.vehicles.remove(vehicle1)
        assertEquals(1, dealer.vehicles.size)
        assertFalse(dealer.vehicles.contains(vehicle1))
        assertTrue(dealer.vehicles.contains(vehicle2))

        // Test setting vehicles
        val newVehicles = listOf(
            Vehicle("truck", "V-003", "$40000", "Ford", "F-150"),
            Vehicle("sports car", "V-004", "$60000", "Chevrolet", "Corvette")
        )

        dealer.vehicles = newVehicles
        assertEquals(2, dealer.vehicles.size)
        assertEquals("V-003", dealer.vehicles[0].id)
        assertEquals("V-004", dealer.vehicles[1].id)
    }

    @Test
    fun testToString() {
        // Add vehicles
        dealer.vehicles.add(Vehicle("sedan", "V-001", "$20000", "Toyota", "Camry"))
        dealer.vehicles.add(Vehicle("suv", "V-002", "$30000", "Honda", "CR-V"))

        // Test toString output
        val output = dealer.toString()

        assertTrue(output.contains("Dealer ID: D-001"))
        assertTrue(output.contains("Name: Test Dealer"))
        assertTrue(output.contains("Toyota Camry"))
        assertTrue(output.contains("Honda CR-V"))
    }
}