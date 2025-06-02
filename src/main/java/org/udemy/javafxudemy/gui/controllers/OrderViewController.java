package org.udemy.javafxudemy.gui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.udemy.javafxudemy.Main;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.gui.util.Alerts;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.entities.OrderDetail;
import org.udemy.javafxudemy.model.entities.Product;
import org.udemy.javafxudemy.model.services.ClientService;
import org.udemy.javafxudemy.model.services.ProductService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderViewController implements Initializable, DataChangeListener {

    private ClientService clientService;
    private ProductService productService = new ProductService();

    //Clients
    @FXML private TableView<Client> tableViewClient;
    @FXML private TableColumn<Client, Integer> tableColumnClientId;
    @FXML private TableColumn<Client, String> tableColumnClientName;
    @FXML private TableColumn<Client, String> tableColumnClientPhone;
    @FXML private TableColumn<Client, String> tableColumnClientEmail;
    @FXML private TableColumn<Client, String> tableColumnClientAddress;
    @FXML private TableColumn<Client, String> tableColumnClientCpf;

    @FXML private TextField txtClientSearch;
    @FXML private Button btSearchClient;

    @FXML private Label lblClientName;
    @FXML private Label lblClientEmail;
    @FXML private Label lblClientPhone;

    private ObservableList<Client> clientObsList;

    //Products list
    @FXML private TableView<Product> tableViewProducts;
    @FXML private TableColumn<Product, Integer> tableColumnProductId;
    @FXML private TableColumn<Product, String> tableColumnProductName;
    @FXML private TableColumn<Product, Double> tableColumnProductPrice;


    @FXML private Label lblSelectedProduct;
    @FXML private Button btAddProduct;

    private ObservableList<Product> productObsList;


    //Order
    @FXML private TableView<OrderDetail> tableViewOrder;
    @FXML private TableColumn<OrderDetail, String> tableColumnProduct;
    @FXML private TableColumn<OrderDetail, Integer> tableColumnQuantity;
    @FXML private TableColumn<OrderDetail, Double> tableColumnUnitPrice;
    @FXML private TableColumn<OrderDetail, Double> tableColumnTotalPrice;
    @FXML private TableColumn<OrderDetail, OrderDetail> tableColumnREMOVE;

    @FXML private Label lblOrderTotal;


    private ObservableList<OrderDetail> orderDetailList = FXCollections.observableArrayList();

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
                    clientObsList = FXCollections.observableArrayList(foundClients);
                    tableViewClient.setItems(clientObsList);
                }
            } catch (Exception e) {
                Alerts.showAlert("Error", "Search error", e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            updateTableView();
        }
    }

    @FXML
    public void onBtAddProductAction() {
        Product selected = tableViewProducts.getSelectionModel().getSelectedItem();

        if (selected != null) {
            OrderDetail existing = orderDetailList.stream()
                    .filter(detail -> detail.getProduct().getId().equals(selected.getId()))
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                int newQuantity = existing.getQuantity() + 1;
                existing.setQuantity(newQuantity);
                existing.setLineTotal(newQuantity * existing.getUnitPrice());
                tableViewOrder.refresh();
                updateOrderTotal();
            } else {
                OrderDetail newDetail = new OrderDetail();
                newDetail.setProduct(selected);
                newDetail.setQuantity(1);
                newDetail.setUnitPrice(selected.getUnitPrice());
                newDetail.setLineTotal(selected.getUnitPrice());
                orderDetailList.add(newDetail);
                updateOrderTotal();
            }
        } else {
            Alerts.showAlert("Produto não selecionado", null, "Por favor, selecione um produto para adicionar ao pedido.", Alert.AlertType.WARNING);
        }
    }


    @FXML
    private void handleClientSelection(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Client selected = tableViewClient.getSelectionModel().getSelectedItem();
            if (selected != null) {
                loadClientData(selected);
            }
        }
    }

    @FXML void handleProductSelection(MouseEvent event){
        if(event.getClickCount() == 1){
            Product selected = tableViewProducts.getSelectionModel().getSelectedItem();
            if(selected != null){
                lblSelectedProduct.setText(selected.getName());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        initRemoveButtons();
    }

    private void loadClientData(Client client) {
        lblClientName.setText(client.getName());
        lblClientEmail.setText(client.getEmail());
        lblClientPhone.setText(client.getPhone());

        //disableFormFields();
    }


    private void initializeNodes(){
        //Table View Client
        tableColumnClientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnClientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnClientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableColumnClientEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnClientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tableColumnClientCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewClient.prefHeightProperty().bind(stage.heightProperty());

        //Table View Product
        tableColumnProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnProductPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));

        //Table View Order Detail
        tableColumnProduct.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getProduct().getName()));

        tableColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableColumnUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableColumnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("lineTotal"));

        tableViewOrder.setItems(orderDetailList);


    }

    private void updateOrderTotal() {
        double total = orderDetailList.stream()
                .mapToDouble(OrderDetail::getLineTotal)
                .sum();
        lblOrderTotal.setText(String.format("Total: R$ %.2f", total));
    }


    public void updateTableView(){
        if(clientService == null) throw new IllegalStateException("Service was null.");

        List<Client> clientList = clientService.findAll();
        clientObsList = FXCollections.observableArrayList(clientList);
        tableViewClient.setItems(clientObsList);

        List<Product> productList = productService.findAll();
        productObsList = FXCollections.observableArrayList(productList);
        tableViewProducts.setItems(productObsList);
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<OrderDetail, OrderDetail>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(OrderDetail detail, boolean empty) {
                super.updateItem(detail, empty);

                if (empty || detail == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> {
                    orderDetailList.remove(detail);
                    updateOrderTotal();
                });
            }
        });
    }


    @Override
    public void onDataChanged() {
        updateTableView();
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }


}
