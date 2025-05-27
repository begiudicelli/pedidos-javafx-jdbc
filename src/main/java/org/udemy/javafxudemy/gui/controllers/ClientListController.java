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
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.gui.util.Utils;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ClientService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    @FXML private TableColumn<Client, Client> tableColumnEDIT;
    @FXML private TableColumn<Client, Client> tableColumnREMOVE;

    @FXML private Button btNewClient;
    @FXML private Button btSearchClient;

    @FXML private TextField txtSearchName;

    private ObservableList<Client> obsList;

    @FXML
    public void onBtNewClientAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Client client = new Client();
        createDialogForm(client, "/org/udemy/javafxudemy/ClientFormView.fxml", parentStage);
    }

    @FXML
    public void onBtSearchClientAction(ActionEvent event) {
        String name = txtSearchName.getText().trim();

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
        tableColumnDateCreated.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewClient.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if(clientService == null) throw new IllegalStateException("Service was null.");

        List<Client> list = clientService.findAll();
        System.out.println("Found " + list.size() + " clients");
        obsList = FXCollections.observableArrayList(list);
        tableViewClient.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }
    private void createDialogForm(Client client, String absolutePath, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            Pane pane = loader.load();

            ClientFormController controller = loader.getController();
            controller.setClient(client);
            controller.setClientService(new ClientService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

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

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Client, Client>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);

                if (client == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                client, "/org/udemy/javafxudemy/ClientFormView.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Client, Client>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);

                if (client == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(client));
            }
        });
    }

    private void removeEntity(Client client){
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if(result.get() == ButtonType.OK){
            if(clientService == null ) throw new IllegalStateException("Service was null");

            try{
                clientService.remove(client);
                updateTableView();
            }catch(DbException e){
                Alerts.showAlert("Error removing client", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}

