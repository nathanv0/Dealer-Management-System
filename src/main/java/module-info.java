module org.example.hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.json;
    requires java.prefs;


    opens org.example.hellofx to javafx.fxml;
    exports org.example.hellofx;
    exports Controller;
    opens Controller to javafx.fxml;
}