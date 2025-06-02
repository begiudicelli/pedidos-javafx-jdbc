package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.OrderDetailDao;
import org.udemy.javafxudemy.model.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDaoJDBC implements OrderDetailDao {

    private final Connection conn;

    public OrderDetailDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(OrderDetail orderDetail) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO SalesOrderDetail (id_order, id_product, quantity, unit_price) " +
                            "VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, orderDetail.getOrder().getId());
            st.setInt(2, orderDetail.getProduct().getId());
            st.setInt(3, orderDetail.getQuantity());
            st.setDouble(4, orderDetail.getUnitPrice());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    orderDetail.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(OrderDetail orderDetail) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE SalesOrderDetail SET id_order = ?, id_product = ?, quantity = ?, unit_price = ? " +
                            "WHERE id_order_detail = ?");

            st.setInt(1, orderDetail.getOrder().getId());
            st.setInt(2, orderDetail.getProduct().getId());
            st.setInt(3, orderDetail.getQuantity());
            st.setDouble(4, orderDetail.getUnitPrice());
            st.setInt(5, orderDetail.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM SalesOrderDetail WHERE id_order_detail = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteByOrderId(Integer orderId) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM SalesOrderDetail WHERE id_order = ?");
            st.setInt(1, orderId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public OrderDetail findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT d.*, p.product_name " +
                            "FROM SalesOrderDetail d " +
                            "INNER JOIN Product p ON d.id_product = p.id_product " +
                            "WHERE d.id_order_detail = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(rs.getInt("id_order_detail"));
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setUnitPrice(rs.getDouble("unit_price"));
                orderDetail.setLineTotal(rs.getDouble("line_total"));
                orderDetail.setProduct(product);

                return orderDetail;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<OrderDetail> findByOrderId(Integer orderId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT d.*, p.product_name, p.unit_price as product_price " +
                            "FROM SalesOrderDetail d " +
                            "INNER JOIN Product p ON d.id_product = p.id_product " +
                            "WHERE d.id_order = ?");

            st.setInt(1, orderId);
            rs = st.executeQuery();
            List<OrderDetail> list = new ArrayList<>();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setUnitPrice(rs.getDouble("product_price"));

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(rs.getInt("id_order_detail"));
                orderDetail.setQuantity(rs.getInt("quantity"));
                orderDetail.setUnitPrice(rs.getDouble("unit_price"));
                orderDetail.setLineTotal(rs.getDouble("line_total"));
                orderDetail.setProduct(product);

                list.add(orderDetail);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
}