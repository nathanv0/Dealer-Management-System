package org.example.groupproject3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

class XMLFileHandlerTest {
    private lateinit var xmlFileHandler: XMLFileHandler
    private lateinit var testXMLFile: File
    private lateinit var sampleDealers: List<Dealer>

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        xmlFileHandler = XMLFileHandler()

        // Create a temporary copy of the sample XML file
        val originalXML = File("src/main/resources/Dealer.xml")
        testXMLFile = File(tempDir.toFile(), "TestDealer.xml")
        Files.copy(originalXML.toPath(), testXMLFile.toPath(), StandardCopyOption.REPLACE_EXISTING)

        // Create test dealers
        val dealer1 = Dealer("D-001", "Dealer One")
        dealer1.vehicles.add(Vehicle("suv", "V-001", "$30000", "Toyota", "RAV4"))
        dealer1.vehicles.add(Vehicle("sedan", "V-002", "$25000", "Honda", "Accord"))

        val dealer2 = Dealer("D-002", "Dealer Two")
        dealer2.vehicles.add(Vehicle("truck", "V-003", "$40000", "Ford", "F-150"))

        sampleDealers = listOf(dealer1, dealer2)
    }


    @Test
    fun testSaveDealers() {
        // Create output file
        val outputFile = File(tempDir.toFile(), "SavedDealers.xml")

        // Save dealers
        xmlFileHandler.saveDealers(sampleDealers, outputFile.absolutePath)

        // Verify file was created
        assertTrue(outputFile.exists())
        assertTrue(outputFile.length() > 0)

        // Load dealers from saved file
        val loadedDealers = xmlFileHandler.getDealers(outputFile.absolutePath)

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
        assertEquals(1, secondDealer.vehicles.size)

        // Check vehicle details (especially price format conversion)
        val vehicle = firstDealer.vehicles[0]
        assertTrue(vehicle.price.contains("£") || vehicle.price.contains("$"))
    }

    @Test
    fun testHandleInvalidFile() {
        // Create an invalid XML file
        val invalidFile = File(tempDir.toFile(), "Invalid.xml")
        invalidFile.writeText("<invalid>This is not valid dealer XML</invalid>")

        // Try to load dealers
        val dealers = xmlFileHandler.getDealers(invalidFile.absolutePath)

        // Should return an empty list, not crash
        assertTrue(dealers.isEmpty())
    }

    @Test
    fun testRoundTripConversion() {
        // Load dealers from the sample file
        val originalDealers = xmlFileHandler.getDealers(testXMLFile.absolutePath)

        // Save them to a new file
        val outputFile = File(tempDir.toFile(), "RoundTrip.xml")
        xmlFileHandler.saveDealers(originalDealers, outputFile.absolutePath)

        // Load them back
        val reloadedDealers = xmlFileHandler.getDealers(outputFile.absolutePath)

        // Verify the count matches
        assertEquals(originalDealers.size, reloadedDealers.size)

        // Compare dealer details
        for (i in originalDealers.indices) {
            val originalDealer = originalDealers[i]
            val reloadedDealer = reloadedDealers[i]

            assertEquals(originalDealer.id, reloadedDealer.id)
            assertEquals(originalDealer.name, reloadedDealer.name)
            assertEquals(originalDealer.vehicles.size, reloadedDealer.vehicles.size)
        }
    }
}