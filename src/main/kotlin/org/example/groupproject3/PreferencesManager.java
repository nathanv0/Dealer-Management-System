package org.example.groupproject3;

import java.io.File;
import java.util.prefs.Preferences;

public class PreferencesManager {
    private static final String LAST_OPENED_FILE = "lastOpenedFile";
    private static final Preferences prefs = Preferences.userNodeForPackage(DealerApplication.class);

    // Save the path of last opened file
    public static void saveLastOpenedFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            prefs.put(LAST_OPENED_FILE, filePath);
            System.out.println("Saved last opened file: " + filePath);
        }
    }

    // Get file path from last opened file
    public static String getLastOpenedFile() {
        String filePath = prefs.get(LAST_OPENED_FILE, null);

        // Verify the file exists before returning it
        if (filePath != null) {
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
        }

        return filePath;
    }

    // Clears the saved last opened file path
    public static void clearLastOpenedFile() {
        prefs.remove(LAST_OPENED_FILE);
    }

}
