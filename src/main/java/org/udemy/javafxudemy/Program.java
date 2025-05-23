package org.udemy.javafxudemy;

import org.udemy.javafxudemy.model.dao.DaoFactory;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;

import java.util.List;

public class Program {

    public static void main(String[] args) {

        ProductDao productDao = DaoFactory.createProductDao();

        System.out.println("=== TESTE 1: product findAll ===");
        List<Product> list = productDao.findAll();
        for (Product p : list) {
            System.out.println(p);
        }
    }
}
