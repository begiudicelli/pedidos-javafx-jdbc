package org.udemy.javafxudemy.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.services.ClientService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderViewController implements Initializable, DataChangeListener {

    private ClientService clientService;

    @FXML private TableView<Client> tableViewClient;
    @FXML private TableColumn<Client, Integer> tableColumnId;
    @FXML private TableColumn<Client, String> tableColumnName;
    @FXML private TableColumn<Client, String> tableColumnPhone;
    @FXML private TableColumn<Client, String> tableColumnEmail;
    @FXML private TableColumn<Client, String> tableColumnAddress;
    @FXML private TableColumn<Client, String> tableColumnCpf;

    @FXML private TextField txtClientSearch;
    @FXML private Button btSearchClient;

    private ObservableList<Client> obsList;

    @FXML
    public void onBtSearchClientAction(ActionEvent event) {
        String name = txtClientSearch.getText().trim();

        if (!name.isEmpty()) {
            try {
                Client searchClient = new Client();
                searchClient.setName(name);
                List<Client> foundClients = clientService.findByName(searchClient);

                if (foundClients.isEmpty()) {
                    Alerts.showAlert("No results", null, "No clients found with name: " + name, Alert.AlertType.INFORMATION);
                } else {
                    obsList = FXCollections.observableArrayList(foundClients);
                    tableViewClient.setItems(obsList);
                }
            } catch (Exception e) {
                Alerts.showAlert("Error", "Search error", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            updateTableView();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }



    private void initializeNodes(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewClient.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(clientService == null) throw new IllegalStateException("Service was null.");

        List<Client> list = clientService.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewClient.setItems(obsList);
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}
