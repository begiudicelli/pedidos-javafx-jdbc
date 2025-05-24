package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {

    private final Connection conn;

    public ProductDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Product obj) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "INSERT INTO product " +
                        "(product_name, unit_price, is_active, created_at) " +
                        "VALUES (?, ?, ?, ?)";

        try{
            st = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            st.setDouble(2, obj.getUnitPrice());
            st.setBoolean(3, obj.getIsActive());
            st.setDate(4, java.sql.Date.valueOf(obj.getCreatedAt()));

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            }else{
                throw new DbException("Unexpected error. No Line affected");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Product obj) {
        PreparedStatement st = null;
        String query = "UPDATE product " +
                        "SET product_name = ?, unit_price = ?, is_active = ? " +
                        "WHERE id_product = ?";

        try {
            st = conn.prepareStatement(query);
            st.setString(1, obj.getName());
            st.setDouble(2, obj.getUnitPrice());
            st.setBoolean(3, obj.getIsActive());
            st.setInt(4, obj.getId());

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Product findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "SELECT * FROM product WHERE id_product = ? ORDER BY product_name";

        try{
            st = conn.prepareStatement(query);
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                return instantiateProduct(rs);
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
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "SELECT * FROM product";
        try{
            st = conn.prepareStatement(query);
            rs = st.executeQuery();

            List<Product> productList = new ArrayList<>();

            while(rs.next()){
                Product product = instantiateProduct(rs);
                productList.add(product);
            }

            return productList;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }


    private Product instantiateProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id_product"));
        product.setName(rs.getString("product_name"));
        product.setCreatedAt(rs.getDate("created_at").toLocalDate());
        product.setUnitPrice(rs.getDouble("unit_price"));
        product.setIsActive(rs.getBoolean("is_active"));
        return product;
    }

}
