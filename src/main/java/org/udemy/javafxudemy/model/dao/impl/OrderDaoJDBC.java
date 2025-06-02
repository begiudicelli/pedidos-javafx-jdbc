package org.udemy.javafxudemy.model.dao.impl;

import org.udemy.javafxudemy.db.DB;
import org.udemy.javafxudemy.db.DbException;
import org.udemy.javafxudemy.model.dao.OrderDao;
import org.udemy.javafxudemy.model.entities.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC implements OrderDao {

    private final Connection conn;

    public OrderDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Order order) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            // Insert order
            st = conn.prepareStatement(
                    "INSERT INTO SalesOrder (order_date, total_price, discount, id_client, payment_method, order_status, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            st.setDouble(2, order.getTotalPrice());
            st.setDouble(3, order.getDiscount());
            st.setInt(4, order.getClient().getId());
            st.setString(5, order.getPaymentMethod().name());
            st.setString(6, order.getOrderStatus().name());
            st.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    order.setId(orderId);
                }
                DB.closeResultSet(rs);

                OrderDetailDaoJDBC detailDao = new OrderDetailDaoJDBC(conn);
                for (OrderDetail item : order.getOrderDetailList()) {
                    item.setOrder(order);
                    detailDao.insert(item);
                }

                conn.commit();
            } else {
                conn.rollback();
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DbException("Error trying to rollback! " + ex.getMessage());
            }
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Order order) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            st = conn.prepareStatement(
                    "UPDATE SalesOrder SET order_date = ?, total_price = ?, discount = ?, id_client = ?, " +
                            "payment_method = ?, order_status = ? WHERE id_order = ?");

            st.setTimestamp(1, Timestamp.valueOf(order.getOrderDate()));
            st.setDouble(2, order.getTotalPrice());
            st.setDouble(3, order.getDiscount());
            st.setInt(4, order.getClient().getId());
            st.setString(5, order.getPaymentMethod().name());
            st.setString(6, order.getOrderStatus().name());
            st.setInt(7, order.getId());

            st.executeUpdate();

            OrderDetailDaoJDBC detailDao = new OrderDetailDaoJDBC(conn);
            detailDao.deleteByOrderId(order.getId());

            for (OrderDetail item : order.getOrderDetailList()) {
                item.setOrder(order);
                detailDao.insert(item);
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DbException("Error trying to rollback! " + ex.getMessage());
            }
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM SalesOrder WHERE id_order = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Order findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT o.*, c.name as client_name, c.email as client_email " +
                            "FROM SalesOrder o " +
                            "INNER JOIN Client c ON o.id_client = c.id_client " +
                            "WHERE o.id_order = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id_client"));
                client.setName(rs.getString("client_name"));
                client.setEmail(rs.getString("client_email"));

                Order order = new Order();
                order.setId(rs.getInt("id_order"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setDiscount(rs.getDouble("discount"));
                order.setClient(client);
                order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                OrderDetailDaoJDBC detailDao = new OrderDetailDaoJDBC(conn);
                order.setOrderDetailList(detailDao.findByOrderId(order.getId()));

                return order;
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
    public List<Order> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT o.*, c.name as client_name, c.email as client_email " +
                            "FROM SalesOrder o " +
                            "INNER JOIN Client c ON o.id_client = c.id_client " +
                            "ORDER BY o.order_date DESC");

            rs = st.executeQuery();
            List<Order> list = new ArrayList<>();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id_client"));
                client.setName(rs.getString("client_name"));
                client.setEmail(rs.getString("client_email"));

                Order order = new Order();
                order.setId(rs.getInt("id_order"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setDiscount(rs.getDouble("discount"));
                order.setClient(client);
                order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(order);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Order> findByClientId(Integer clientId) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT o.*, c.name as client_name, c.email as client_email " +
                            "FROM SalesOrder o " +
                            "INNER JOIN Client c ON o.id_client = c.id_client " +
                            "WHERE o.id_client = ? " +
                            "ORDER BY o.order_date DESC");

            st.setInt(1, clientId);
            rs = st.executeQuery();
            List<Order> list = new ArrayList<>();

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id_client"));
                client.setName(rs.getString("client_name"));
                client.setEmail(rs.getString("client_email"));

                Order order = new Order();
                order.setId(rs.getInt("id_order"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                order.setDiscount(rs.getDouble("discount"));
                order.setClient(client);
                order.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));
                order.setOrderStatus(OrderStatus.valueOf(rs.getString("order_status")));
                order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(order);
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