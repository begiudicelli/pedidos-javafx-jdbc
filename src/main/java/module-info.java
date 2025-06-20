module org.udemy.javafxudemy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.udemy.javafxudemy to javafx.fxml;
    opens org.udemy.javafxudemy.gui.controllers to javafx.fxml;
    opens org.udemy.javafxudemy.model.entities to javafx.fxml, javafx.base;

    exports org.udemy.javafxudemy;
    exports org.udemy.javafxudemy.gui.controllers;
}