package org.udemy.javafxudemy.gui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ProductService;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.gui.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductListController implements Initializable, DataChangeListener {

    private ProductService productService;

    //Table view
    @FXML private TableView<Product> tableViewProduct;
    @FXML private TableColumn<Product, Integer> tableColumnId;
    @FXML private TableColumn<Product, String> tableColumnName;
    @FXML private TableColumn<Product, Double> tableColumnPrice;
    @FXML private TableColumn<Product, Boolean> tableColumnIsActive;
    @FXML private TableColumn<Product, LocalDate> tableColumnDateCreated;
    @FXML private TableColumn<Product, Product> tableColumnEDIT;
    @FXML private TableColumn<Product, Product> tableColumnREMOVE;

    @FXML
    private Button btNewProduct;

    private ObservableList<Product> obsList;

    @FXML
    public void onBtNewProductAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Product product = new Product();
        createDialogForm(product, "/org/udemy/javafxudemy/ProductFormView.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }


    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Product, Product>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (product == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                product, "/org/udemy/javafxudemy/ProductFormView.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Product, Product>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (product == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(product));
            }
        });
    }

    private void removeEntity(Product product){
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if(result.get() == ButtonType.OK){
            if(productService == null ) throw new IllegalStateException("Service was null");

            try{
                productService.remove(product);
                updateTableView();
            }catch(DbException e){
                Alerts.showAlert("Error removing product", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
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
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Product product, String absolutePath, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            Pane pane = loader.load();

            ProductFormController controller = loader.getController();
            controller.setProduct(product);
            controller.setProductService(new ProductService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

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

    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
