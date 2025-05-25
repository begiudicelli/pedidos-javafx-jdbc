package org.udemy.javafxudemy.model.dao;

import org.udemy.javafxudemy.model.entities.Client;

import java.util.List;

public interface ClientDao {
    void insert(Client client);
    void update(Client client);
    void deleteById(Integer id);
    Client findById(Integer id);
    List<Client> findAll();
}
