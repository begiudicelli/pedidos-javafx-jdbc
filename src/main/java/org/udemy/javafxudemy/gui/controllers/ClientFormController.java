package org.udemy.javafxudemy.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.gui.util.Utils;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.exceptions.ValidationException;
import org.udemy.javafxudemy.model.services.ClientService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientFormController {

    private Client client;
    private ClientService clientService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;
    @FXML private TextField txtCpf;

    @FXML private Label labelErrorName;
    @FXML private Label labelErrorPhone;
    @FXML private Label labelErrorEmail;
    @FXML private Label labelErrorAddress;
    @FXML private Label labelErrorCpf;

    @FXML private Button btnSave;
    @FXML private Button btnCancel;


    public void onBtnSaveAction(ActionEvent event) {
        if(client == null) throw new IllegalStateException("Product was null");
        if(clientService == null) throw new IllegalStateException("Product service was null");

        try{
            client = getFormData();
            clientService.saveOrUpdate(client);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }catch(ValidationException e){
            setLabelErrors(e.getErros());
        }
        catch(DbException e){
            Alerts.showAlert("Database exception", "Error saving product", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private Client getFormData() {
        Client client = new Client();
        ValidationException exception = new ValidationException("Validation error");

        client.setId(Utils.tryParseToInt(txtId.getText()));

        if(txtName.getText() == null || txtName.getText().isBlank()){
            exception.addError("name", "Field cant be empty");
        }
        client.setName(txtName.getText());
        client.setPhone(txtPhone.getText());
        client.setEmail(txtEmail.getText());
        client.setAddress(txtAddress.getText());
        client.setCpf(txtCpf.getText());
        client.setCreatedAt(LocalDateTime.now());

        if(!exception.getErros().isEmpty()){
            throw exception;
        }

        return client;
    }

    public void updateFormData(){
        if(client == null) throw new IllegalStateException("Product was null");
        txtId.setText(String.valueOf(client.getId()));
        txtName.setText(client.getName());
        txtPhone.setText(client.getPhone());
        txtEmail.setText(client.getEmail());
        txtAddress.setText(client.getAddress());
        txtCpf.setText(client.getCpf());
    }

    private void setLabelErrors(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if(fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }

    public void subscribeDataChangeListener(DataChangeListener listener){
        dataChangeListeners.add(listener);
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void setClientService(ClientService clientService){
        this.clientService = clientService;
    }

}
