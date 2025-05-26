package org.udemy.javafxudemy.gui.controllers;

import javafx.event.ActionEvent;
import org.udemy.javafxudemy.gui.listeners.DataChangeListener;
import org.udemy.javafxudemy.model.entities.Client;
import org.udemy.javafxudemy.model.services.ClientService;

import java.util.ArrayList;
import java.util.List;

public class ClientFormController {

    private Client client;
    private ClientService clientService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();



    public void onBtnSaveAction(ActionEvent event) {
    }

    public void onBtnCancelAction(ActionEvent event) {
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
