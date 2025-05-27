package org.udemy.javafxudemy.model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDateTime orderDate;
    private Double totalPrice;
    private Double discount;
    private Client client;
    private PaymentMethod  paymentMethod;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private List<OrderDetail> orderDetailList = new ArrayList<>();

    public Order(){

    }

    public Order(Integer id, LocalDateTime orderDate, Double totalPrice, Double discount, Client client,
                 PaymentMethod paymentMethod, OrderStatus orderStatus, LocalDateTime createdAt, List<OrderDetail> orderDetailList) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.client = client;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.orderDetailList = orderDetailList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", discount=" + discount +
                ", client=" + client +
                ", paymentMethod=" + paymentMethod +
                ", orderStatus=" + orderStatus +
                ", createdAt=" + createdAt +
                '}';
    }
}
