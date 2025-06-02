package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.ProductDao;
import org.udemy.javafxudemy.model.entities.Product;

import java.sql.*;
import java.util.*;

public class ProductDaoJDBC implements ProductDao {

    private final Connection conn;
    private final Map<Integer, Product> productCache = new HashMap<>();

    public ProductDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Product obj) {
        String sql = "INSERT INTO product (product_name, unit_price, is_active) VALUES (?, ?, ?)";

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setStatementParameters(st, obj);

            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Insert failed: no rows affected.");
            }

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                    productCache.put(id, obj);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error inserting product: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Product obj) {
        String sql = "UPDATE product SET product_name = ?, unit_price = ?, is_active = ? WHERE id_product = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            setStatementParameters(st, obj);
            st.setInt(4, obj.getId());
            st.executeUpdate();

            productCache.put(obj.getId(), obj);
        } catch (SQLException e) {
            throw new DbException("Error updating product: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM product WHERE id_product = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            int rows = st.executeUpdate();

            if (rows == 0) {
                throw new DbException("Delete failed: no product found with ID " + id);
            }

            productCache.remove(id);
        } catch (SQLException e) {
            throw new DbException("Error deleting product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product findById(Integer id) {
        if (productCache.containsKey(id)) {
            return productCache.get(id);
        }

        String sql = "SELECT * FROM product WHERE id_product = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Product product = instantiateProduct(rs);
                    productCache.put(product.getId(), product);
                    return product;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Error finding product by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product ORDER BY product_name";
        List<Product> list = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_product");

                if (productCache.containsKey(id)) {
                    list.add(productCache.get(id));
                } else {
                    Product product = instantiateProduct(rs);
                    productCache.put(id, product);
                    list.add(product);
                }
            }

            return list;
        } catch (SQLException e) {
            throw new DbException("Error fetching all products: " + e.getMessage(), e);
        }
    }

    // Utilitários

    private void setStatementParameters(PreparedStatement st, Product obj) throws SQLException {
        st.setString(1, obj.getName());
        st.setDouble(2, obj.getUnitPrice());
        st.setBoolean(3, obj.getIsActive());
    }

    private Product instantiateProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id_product"));
        product.setName(rs.getString("product_name"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUnitPrice(rs.getDouble("unit_price"));
        product.setIsActive(rs.getBoolean("is_active"));
        return product;
    }
}
