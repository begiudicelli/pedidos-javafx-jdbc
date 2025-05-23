package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private Connection conn;

    public ProductDaoJDBC(Connection conn){
        this.conn = conn;
    }

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
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM product " +
                            "WHERE id_product = ?";

            st = conn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                Product product = new Product();
                product.setId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setCreated_at(rs.getDate("created_at").toLocalDate());
                product.setUnitPrice(rs.getDouble("unit_price"));
                product.setIsActive(rs.getInt("is_active"));
                return product;
            }

            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Product> findAll() {
        //Mock
        return null;
    }

}
