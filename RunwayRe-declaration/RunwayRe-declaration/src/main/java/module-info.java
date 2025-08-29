module uk.ac.soton.comp2211.runwayredeclaration {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.desktop;
    requires java.sql;
    requires javafx.media;
    requires pdfbox;
    requires javafx.swing;



    opens uk.ac.soton.comp2211.runwayredeclaration to javafx.fxml;
    exports uk.ac.soton.comp2211.runwayredeclaration;
    exports uk.ac.soton.comp2211.runwayredeclaration.Component;
    exports uk.ac.soton.comp2211.runwayredeclaration.Calculator;
}