package org.udemy.javafxudemy;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.dao.impl.ProductDaoJDBC;
import org.udemy.javafxudemy.model.entities.Product;

import java.time.LocalDate;

public class Program {
    public static void main(String[] args) {
        // Obtém a conexão com o banco de dados
        try {
            var conn = DB.getConnection(); // Certifique-se que DB.getConnection() está implementado

            // Cria um DAO para inserção de produtos
            ProductDao productDao = new ProductDaoJDBC(conn);

            // Cria um produto fictício para inserir
            Product newProduct = new Product();
            newProduct.setName("Samsung Galaxy 13");
            newProduct.setUnitPrice(899.0);
            newProduct.setIsActive(true);
            newProduct.setCreatedAt(LocalDate.now());
            productDao.insert(newProduct);

            System.out.println("Produto inserido com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        } finally {
            DB.closeConnection();
        }
    }
}
