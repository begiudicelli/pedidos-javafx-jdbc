package org.udemy.javafxudemy.model.services;

import org.udemy.javafxudemy.model.dao.DaoFactory;
import org.udemy.javafxudemy.model.dao.OrderDao;
import org.udemy.javafxudemy.model.entities.Order;

public class OrderService {
    private final OrderDao dao = DaoFactory.createOrderDao();


    public void save(Order order){
        dao.insert(order);
    }

}
