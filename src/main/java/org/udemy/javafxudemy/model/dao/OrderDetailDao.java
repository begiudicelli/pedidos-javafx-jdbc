package org.udemy.javafxudemy.model.dao;

import org.udemy.javafxudemy.model.entities.OrderDetail;
import java.util.List;

public interface OrderDetailDao {
    void insert(OrderDetail orderDetail);
    void update(OrderDetail orderDetail);
    void deleteById(Integer id);
    OrderDetail findById(Integer id);
    List<OrderDetail> findByOrderId(Integer orderId);
    void deleteByOrderId(Integer orderId);
}