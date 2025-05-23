module org.udemy.javafxudemy {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.udemy.javafxudemy to javafx.fxml;
    exports org.udemy.javafxudemy;
}