package org.example.groupproject3;

import java.util.List;

public class DealerManager {
    // Single instance
    private static DealerManager instance;

    // The list of dealers held by singleton
    private static List<Dealer> dealers;

    // Private constructor to prevent instantiation
    private DealerManager() {}

    public static synchronized DealerManager getInstance() {
        if (instance == null) {
            instance = new DealerManager();
        }
        return instance;
    }

    public List<Dealer> loadDealers(String filename) {
        // If dealers not exist -> create one. Otherwise, return only existing dealers
        if (dealers == null) {
            String extension = getFileExtension(filename);

            if (extension.equals("json")) {
                JSONFileHandler jsonFileHandler = new JSONFileHandler();
                dealers = jsonFileHandler.getDealers(filename);
            } else if (extension.equals("xml")) {
                XMLFileHandler readXMLFile = new XMLFileHandler();
                dealers = readXMLFile.getDealers(filename);
            } else {
                System.out.println("This file type is not supported!!!");
            }
        }
        return dealers;
    }

    // Check file extension: XML or JSON
    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static void setDealers(List<Dealer> newDealers) {
        dealers = newDealers;
    }
}
