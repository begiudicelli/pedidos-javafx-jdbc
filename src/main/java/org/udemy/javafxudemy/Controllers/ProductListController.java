package org.udemy.javafxudemy.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.Model.entities.Product;
import org.udemy.javafxudemy.Model.services.ProductService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductListController implements Initializable {

    private ProductService productService;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private TableColumn<Product, Integer> tableColumnId;

    @FXML
    private TableColumn<Product, String> tableColumnName;

    @FXML
    private Button btNewProduct;

    private ObservableList<Product> obsList;

    @FXML
    public void onBtNewProductAction(){
        //TODO: handle new product in database
        System.out.println("bt new product working");
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

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewProduct.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(productService == null) throw new IllegalStateException("Service was null.");

        List<Product> list = productService.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewProduct.setItems(obsList);
    }
}
