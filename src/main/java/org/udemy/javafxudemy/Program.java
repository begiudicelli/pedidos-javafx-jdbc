package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.dao.impl.ProductDaoJDBC;
import org.udemy.javafxudemy.model.entities.Product;

import java.sql.Connection;

public class Program {
    public static void main(String[] args) {
        Connection conn = null;

        try {
            conn = DB.getConnection();
            ProductDao productDao = new ProductDaoJDBC(conn);

            Product product = productDao.findById(9);

            if (product != null) {
                System.out.println("Produto encontrado: " + product.getName());
                System.out.println("Preço atual: R$" + product.getUnitPrice());

                product.setUnitPrice(product.getUnitPrice() + 100.00);

                productDao.update(product);

                System.out.println("Preço atualizado com sucesso!");
            } else {
                System.out.println("Produto com id = 9 não encontrado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.closeConnection();
        }
    }
}
