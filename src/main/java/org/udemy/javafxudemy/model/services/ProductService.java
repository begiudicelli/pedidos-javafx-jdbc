package org.udemy.javafxudemy.model.services;

import org.udemy.javafxudemy.model.dao.DaoFactory;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.dao.impl.ProductDaoJDBC;
import org.udemy.javafxudemy.model.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private final ProductDao dao = DaoFactory.createProductDao();

    public List<Product> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate(Product product){
        if(product.getId() == null){
            dao.insert(product);
        }else{
            dao.update(product);
        }
    }
}
