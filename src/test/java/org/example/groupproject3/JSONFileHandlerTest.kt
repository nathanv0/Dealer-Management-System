package org.example.groupproject3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class JSONFileHandlerTest {
    private lateinit var jsonFileHandler: JSONFileHandler
    private lateinit var sampleDealers: List<Dealer>

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        jsonFileHandler = JSONFileHandler()

        // Create test dealers
        val dealer1 = Dealer("D-001", "Dealer One")
        dealer1.vehicles.add(Vehicle("suv", "V-001", "$30000", "Toyota", "RAV4"))
        dealer1.vehicles.add(Vehicle("sedan", "V-002", "$25000", "Honda", "Accord"))

        val dealer2 = Dealer("D-002", "Dealer Two")
        dealer2.vehicles.add(Vehicle("truck", "V-003", "$40000", "Ford", "F-150"))
        dealer2.vehicles.add(Vehicle("sports car", "V-004", "$60000", "Chevrolet", "Corvette"))

        sampleDealers = listOf(dealer1, dealer2)
    }

    @Test
    fun testSaveAndGetDealers() {
        // Create output file
        val outputFile = File(tempDir.toFile(), "Dealers.json")

        // Save dealers
        jsonFileHandler.saveDealers(sampleDealers, outputFile.absolutePath)

        // Verify file was created
        assertTrue(outputFile.exists())
        assertTrue(outputFile.length() > 0)

        // Load dealers from saved file
        val loadedDealers = jsonFileHandler.getDealers(outputFile.absolutePath)

        // Verify loaded dealers match original
        assertEquals(sampleDealers.size, loadedDealers.size)

        // Check first dealer
        val firstDealer = loadedDealers[0]
        assertEquals("D-001", firstDealer.id)
        assertEquals("Dealer One", firstDealer.name)
        assertEquals(2, firstDealer.vehicles.size)

        // Check second dealer
        val secondDealer = loadedDealers[1]
        assertEquals("D-002", secondDealer.id)
        assertEquals("Dealer Two", secondDealer.name)
        assertEquals(2, secondDealer.vehicles.size)

        // Check vehicle details
        val vehicle = firstDealer.vehicles[0]
        assertEquals("V-001", vehicle.id)
        assertEquals("suv", vehicle.type)
        assertEquals("Toyota", vehicle.make)
        assertEquals("RAV4", vehicle.model)
        assertEquals("$30000", vehicle.price)
        assertEquals("Available", vehicle.status)
    }

    @Test
    fun testVehicleStatus() {
        // Modify vehicle status
        val vehicle = sampleDealers[0].vehicles[0]
        vehicle.status = "Rented"

        // Create output file
        val outputFile = File(tempDir.toFile(), "DealersWithStatus.json")

        // Save dealers
        jsonFileHandler.saveDealers(sampleDealers, outputFile.absolutePath)

        // Load dealers from saved file
        val loadedDealers = jsonFileHandler.getDealers(outputFile.absolutePath)

        // Check vehicle status was preserved
        val loadedVehicle = loadedDealers[0].vehicles[0]
        assertEquals("Rented", loadedVehicle.status)
    }

    @Test
    fun testInvalidJson() {
        // Create an invalid JSON file
        val invalidFile = File(tempDir.toFile(), "Invalid.json")
        invalidFile.writeText("{\"This is not valid JSON\": ")

        // Try to load dealers
        val dealers = jsonFileHandler.getDealers(invalidFile.absolutePath)

        // Should return an empty list, not crash
        assertTrue(dealers.isEmpty())
    }

    @Test
    fun testEmptyJson() {
        // Create an empty JSON file
        val emptyFile = File(tempDir.toFile(), "Empty.json")
        emptyFile.writeText("")

        // Try to load dealers
        val dealers = jsonFileHandler.getDealers(emptyFile.absolutePath)

        // Should return an empty list, not crash
        assertTrue(dealers.isEmpty())
    }

    @Test
    fun testMalformedJson() {
        // Create a valid JSON file but with incorrect structure for our app
        val malformedFile = File(tempDir.toFile(), "Malformed.json")
        malformedFile.writeText("{\"SomethingElse\": [1, 2, 3]}")

        // Try to load dealers
        val dealers = jsonFileHandler.getDealers(malformedFile.absolutePath)

        // Should return an empty list, not crash
        assertTrue(dealers.isEmpty())
    }

    @Test
    fun testRoundTripConversion() {
        // Create output file for first save
        val outputFile1 = File(tempDir.toFile(), "FirstSave.json")

        // Save dealers
        jsonFileHandler.saveDealers(sampleDealers, outputFile1.absolutePath)

        // Load dealers from saved file
        val loadedDealers = jsonFileHandler.getDealers(outputFile1.absolutePath)

        // Create output file for second save
        val outputFile2 = File(tempDir.toFile(), "SecondSave.json")

        // Save loaded dealers
        jsonFileHandler.saveDealers(loadedDealers, outputFile2.absolutePath)

        // Load dealers from second saved file
        val reloadedDealers = jsonFileHandler.getDealers(outputFile2.absolutePath)

        // Verify the count matches
        assertEquals(sampleDealers.size, reloadedDealers.size)

        // Compare dealer details
        for (i in sampleDealers.indices) {
            val originalDealer = sampleDealers[i]
            val reloadedDealer = reloadedDealers[i]

            assertEquals(originalDealer.id, reloadedDealer.id)
            assertEquals(originalDealer.name, reloadedDealer.name)
            assertEquals(originalDealer.vehicles.size, reloadedDealer.vehicles.size)
        }
    }
}