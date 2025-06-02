package org.udemy.javafxudemy.model.dao;

import org.udemy.javafxudemy.model.entities.Order;
import java.util.List;

public interface OrderDao {
    void insert(Order order);
    void update(Order order);
    void deleteById(Integer id);
    Order findById(Integer id);
    List<Order> findAll();
    List<Order> findByClientId(Integer clientId);
}