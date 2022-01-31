module com.example.photoeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.scripting;
    requires opencv;
    requires javafx.swing;

    opens com.example.photoeditor to javafx.fxml;
    exports com.example.photoeditor;
}