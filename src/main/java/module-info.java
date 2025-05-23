module org.udemy.javafxudemy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.udemy.javafxudemy to javafx.fxml;
    opens org.udemy.javafxudemy.controllers to javafx.fxml;
    opens org.udemy.javafxudemy.model.entities to javafx.fxml, javafx.base;

    exports org.udemy.javafxudemy;
    exports org.udemy.javafxudemy.controllers;
}