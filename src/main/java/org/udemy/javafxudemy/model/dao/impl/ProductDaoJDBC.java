package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;

import java.util.List;

public class ProductDaoJDBC implements ProductDao {




    @Override
    public void insert(Product obj) {

    }

    @Override
    public void update(Product obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Product findById(Integer id) {
        return null;
    }

    @Override
    public List<Product> findAll() {
        //Mock
        Product p1 = new Product(1, "Notebook");
        Product p2 = new Product(2, "Mouse");
        Product p3 = new Product(3, "Teclado");
        return List.of(p1, p2, p3);
    }

}
