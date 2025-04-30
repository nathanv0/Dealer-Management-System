module org.example.groupproject3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.json;
    requires java.prefs;
    requires java.xml;


    opens org.example.groupproject3 to javafx.fxml;
    exports org.example.groupproject3;
    exports Controller;
    opens Controller to javafx.fxml;
}