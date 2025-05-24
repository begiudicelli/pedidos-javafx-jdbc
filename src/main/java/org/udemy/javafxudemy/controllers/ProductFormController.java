package org.udemy.javafxudemy.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ProductService;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.gui.util.Constraints;
import org.udemy.javafxudemy.gui.util.Utils;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    private Product product;
    private ProductService productService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();


    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;

    @FXML private Label labelErrorName;
    @FXML private Label labelErrorPrice;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;


    @FXML
    public void onBtnSaveAction(ActionEvent event){
        if(product == null) throw new IllegalStateException("Product was null");
        if(productService == null) throw new IllegalStateException("Product service was null");

        try{
            product = getFormData();
            productService.saveOrUpdate(product);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch(DbException e){
            Alerts.showAlert("Database exception", "Error saving product", e.getMessage(), Alert.AlertType.ERROR);
        }
    }



    private Product getFormData() {
        Product product = new Product();
        product.setId(Utils.tryParseToInt(txtId.getText()));
        product.setName(txtName.getText());
        product.setUnitPrice(Utils.tryParseToDouble(txtPrice.getText()));

        product.setIsActive(true);
        product.setCreatedAt(LocalDate.now());

        return product;
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event){
        Utils.currentStage(event).close();
    }


    public void updateFormData(){
        if(product == null) throw new IllegalStateException("Product was null");
        txtId.setText(String.valueOf(product.getId()));
        txtName.setText(product.getName());
        txtPrice.setText(String.valueOf(product.getUnitPrice()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }


    private void initializeNodes(){
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldTextOnly(txtName);
        Constraints.setTextFieldMaxLength(txtName, 30);
        Constraints.setTextFieldDouble(txtPrice);
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    public void setProduct(Product product){
        this.product = product;
    }

    public void setProductService(ProductService productService){
        this.productService = productService;
    }
}
