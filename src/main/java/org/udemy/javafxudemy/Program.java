package org.udemy.javafxudemy;

import org.udemy.javafxudemy.model.dao.DaoFactory;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;


public class Program {

    public static void main(String[] args) {

        ProductDao productDao = DaoFactory.createProductDao();

        Product product = productDao.findById(1);

        System.out.println(product);
    }
}
