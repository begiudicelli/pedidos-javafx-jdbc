package org.udemy.javafxudemy.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.udemy.javafxudemy.util.Constraints;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;

    @FXML private Label labelErrorName;
    @FXML private Label labelErrorPrice;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    @FXML
    public void onBtnSaveAction(){
        System.out.println("onBtnSaveAction");
    }

    @FXML
    public void onBtnCancelAction(){
        System.out.println("onBtnCancelAction");
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
}
