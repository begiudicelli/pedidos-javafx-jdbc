package org.udemy.javafxudemy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.model.services.ProductService;
import org.udemy.javafxudemy.gui.util.Alerts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemClient;

    @FXML
    private MenuItem menuItemProduct;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemClientAction(){
        //TODO: Handle the action
        System.out.println("On menu item client");
    }

    @FXML
    public void onMenuItemProductAction(){
        loadView("/org/udemy/javafxudemy/ProductListView.fxml", (ProductListController controller)->{
            controller.setProductService(new ProductService());
            controller.updateTableView();
        });
    }
    @FXML
    public void onMenuItemAboutAction(){
        loadView("/org/udemy/javafxudemy/AboutView.fxml", x -> {});
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void loadView(String absolutePath, Consumer<T> initializingAction){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().getFirst();
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
