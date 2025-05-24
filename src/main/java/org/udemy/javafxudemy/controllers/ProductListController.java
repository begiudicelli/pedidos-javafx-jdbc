package org.udemy.javafxudemy.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ProductService;
import org.udemy.javafxudemy.util.Alerts;
import org.udemy.javafxudemy.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ProductListController implements Initializable {

    private ProductService productService;

    //Table view
    @FXML private TableView<Product> tableViewProduct;
    @FXML private TableColumn<Product, Integer> tableColumnId;
    @FXML private TableColumn<Product, String> tableColumnName;
    @FXML private TableColumn<Product, Double> tableColumnPrice;
    @FXML private TableColumn<Product, Boolean> tableColumnIsActive;
    @FXML private TableColumn<Product, LocalDate> tableColumnDateCreated;

    @FXML
    private Button btNewProduct;

    private ObservableList<Product> obsList;

    @FXML
    public void onBtNewProductAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        createDialogForm("/org/udemy/javafxudemy/ProductForm.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    private void initializeNodes(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableColumnIsActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tableColumnDateCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewProduct.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(productService == null) throw new IllegalStateException("Service was null.");

        List<Product> list = productService.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewProduct.setItems(obsList);
    }

    private void createDialogForm(String absolutePath, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            Pane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter product data.");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
