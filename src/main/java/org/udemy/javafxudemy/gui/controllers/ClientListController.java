package org.udemy.javafxudemy.gui.controllers;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.gui.util.Utils;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ClientService;

import javafx.scene.control.TableView;
import org.udemy.javafxudemy.model.services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ClientListController implements Initializable, DataChangeListener {
    private ClientService clientService;

    @FXML private TableView<Client> tableViewClient;
    @FXML private TableColumn<Client, Integer> tableColumnId;
    @FXML private TableColumn<Client, String> tableColumnName;
    @FXML private TableColumn<Client, String> tableColumnPhone;
    @FXML private TableColumn<Client, String> tableColumnEmail;
    @FXML private TableColumn<Client, String> tableColumnAddress;
    @FXML private TableColumn<Client, String> tableColumnCpf;
    @FXML private TableColumn<Client, LocalDateTime> tableColumnDateCreated;

    @FXML private Button btNewClient;

    private ObservableList<Client> obsList;

    @FXML
    public void onBtNewClientAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Client client = new Client();
        createDialogForm(client, "/org/udemy/javafxudemy/ClientFormView.fxml", parentStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void createDialogForm(Client client, String absolutePath, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            Pane pane = loader.load();

            ClientFormController controller = loader.getController();
            controller.setClient(client);
            controller.setClientService(new ClientService());
            controller.subscribeDataChangeListener(this);
            //controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Client data.");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void initializeNodes(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnDateCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewClient.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(clientService == null) throw new IllegalStateException("Service was null.");

        List<Client> list = clientService.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewClient.setItems(obsList);
        //initEditButtons();
        //initRemoveButtons();
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}

