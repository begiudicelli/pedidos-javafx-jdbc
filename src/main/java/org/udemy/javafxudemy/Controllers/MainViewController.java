package org.udemy.javafxudemy.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemClient;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemClientAction(){
        //TODO: Handle the action
        System.out.println("On menu item client");
    }

    @FXML
    public void onMenuItemAboutAction(){
        //TODO: Handle the action
        System.out.println("On menu item about");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
