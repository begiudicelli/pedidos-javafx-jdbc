package org.udemy.javafxudemy.Model.services;

import org.udemy.javafxudemy.Model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public List<Product> findAll(){
        //MOCK
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Snack bar"));
        products.add(new Product(2, "Ice cream"));
        products.add(new Product(3, "Apple pie"));

        return products;
    }
}
