package org.example.groupproject3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLFileHandler implements FileHandler{
    public List<Dealer> getDealers(String fileName) {
        // List to store dealers
        List<Dealer> dealers = new ArrayList<>();

        // Document Builder Factory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Document Builder
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the xml file: read the file and extract information
            Document document =  builder.parse(new File(fileName));

            // Create Node list to store each dealer
            NodeList dealerList = document.getElementsByTagName("Dealer");
            System.out.println("Total Dealers: " + dealerList.getLength() + "\n");

            // Iterate through the Node list and get Node element and attributes
            for (int i = 0; i < dealerList.getLength(); i++) {
                Node node = dealerList.item(i);
                // Check node type
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute("id");
                    System.out.println("Dealership ID: " + id);

                    String name = element.getElementsByTagName("Name").item(0).getTextContent();

                    // Create a list of vehicles
                    List<Vehicle> vehicles = new ArrayList<>();

                    // Get list of vehicle
                    NodeList vehicleList = element.getElementsByTagName("Vehicle");
                    System.out.println("Total vehicle per dealer: " + vehicleList.getLength());
                    for (int j = 0; j < vehicleList.getLength(); j++) {
                        // Get all vehicle content
                        Element vehicleElement = (Element) vehicleList.item(j);
                        String type = vehicleElement.getAttribute("type");
                        String vehicleId = vehicleElement.getAttribute("id");
                        Element priceElement = (Element) vehicleElement.getElementsByTagName("Price").item(0);
                        String price = priceElement.getTextContent();
                        String unit = priceElement.getAttribute("unit");
                        String unitPrice = "£ " + price;
                        if (unit.equals("dollars")) {
                            unitPrice = "$ " + price;
                        }
                        String make = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
                        String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();

                        // Create a new vehicle object
                        Vehicle vehicle = new Vehicle(type, vehicleId, unitPrice, make, model);
                        vehicles.add(vehicle);
                    }
                    // Create dealer object and add vehicle to the list
                    Dealer dealer = new Dealer(id, name);
                    dealer.setVehicles(vehicles);
                    dealers.add(dealer);

                    // Print out the dealer and vehicles information
                    for (Vehicle v : vehicles) {
                        System.out.println("Vehicle: " + v.getMake() + " " + v.getModel() + " (" + v.getType() + ")");
                    }
                    System.out.println();
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return dealers;

    }

    @Override
    public void saveDealers(List<Dealer> dealers, String outputPath) {
        try {
            // Document Builder Factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Builder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create new document
            Document document = builder.newDocument();

            // Root element
            Element root = document.createElement("Dealers");
            document.appendChild(root);

            // Loop through each dealer and set the attributes
            for (Dealer dealer : dealers) {
                Element dealerElement = document.createElement("Dealer");
                dealerElement.setAttribute("id", dealer.getId());

                // Dealer name
                Element nameElement = document.createElement("Name");
                nameElement.setTextContent(dealer.getName());
                dealerElement.appendChild(nameElement);

                // Vehicles
                for (Vehicle vehicle : dealer.getVehicles()) {
                    Element vehicleElement = document.createElement("Vehicle");
                    vehicleElement.setAttribute("type", vehicle.getType());
                    vehicleElement.setAttribute("id", vehicle.getId());

                    // Price element with unit handling
                    Element priceElement = document.createElement("Price");

                    // Extract unit and price value
                    String unit = vehicle.getPrice().contains("£") ? "pounds" : "dollars";
                    String value = vehicle.getPrice().replace("£ ", "").replace("$ ", "");

                    priceElement.setAttribute("unit", unit);
                    priceElement.setTextContent(value);
                    vehicleElement.appendChild(priceElement);

                    // Make
                    Element makeElement = document.createElement("Make");
                    makeElement.setTextContent(vehicle.getMake());
                    vehicleElement.appendChild(makeElement);

                    // Model
                    Element modelElement = document.createElement("Model");
                    modelElement.setTextContent(vehicle.getModel());
                    vehicleElement.appendChild(modelElement);

                    // Add vehicle to dealer
                    dealerElement.appendChild(vehicleElement);
                }

                // Add dealer to root
                root.appendChild(dealerElement);
            }

            // Write to file
            // Use xml builder library to write to xml file
            // I actually did used AI to help me with this part because I couldn't figure out how to write it to xml file.
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-space", "4");

            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new File(outputPath));

            transformer.transform(source, result);

            System.out.println("Dealers saved to: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
