module org.udemy.javafxudemy {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.udemy.javafxudemy to javafx.fxml;
    opens org.udemy.javafxudemy.Controllers to javafx.fxml;
    opens org.udemy.javafxudemy.Model.entities to javafx.fxml, javafx.base;

    exports org.udemy.javafxudemy;
    exports org.udemy.javafxudemy.Controllers;
}