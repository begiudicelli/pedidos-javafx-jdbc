package org.udemy.javafxudemy.model.dao;

import org.udemy.javafxudemy.model.dao.impl.ProductDaoJDBC;

public class DaoFactory {

    public static ProductDao createProductDao(){
        return new ProductDaoJDBC();
    }
}
