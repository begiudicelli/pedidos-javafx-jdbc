package org.udemy.javafxudemy.model.dao;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.impl.ClientDaoJDBC;
import org.udemy.javafxudemy.model.dao.impl.OrderDaoJDBC;
import org.udemy.javafxudemy.model.dao.impl.ProductDaoJDBC;

public class DaoFactory {

    public static ProductDao createProductDao(){
        return new ProductDaoJDBC(DB.getConnection());
    }

    public static ClientDao createClientDao(){
        return new ClientDaoJDBC(DB.getConnection());
    }

    public static OrderDao createOrderDao(){
        return new OrderDaoJDBC(DB.getConnection());
    }
}
