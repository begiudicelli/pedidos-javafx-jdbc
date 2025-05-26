package org.udemy.javafxudemy.model.services;

import org.udemy.javafxudemy.model.dao.ClientDao;
import org.udemy.javafxudemy.model.dao.DaoFactory;
import org.udemy.javafxudemy.model.entities.Client;

import java.util.List;

public class ClientService {

    private final ClientDao dao = DaoFactory.createClientDao();

    public List<Client> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Client client){
        if(client.getId() == null){
            dao.insert(client);
        }else{
            dao.update(client);
        }
    }

    public void remove(Client client){
        dao.deleteById(client.getId());
    }
}
