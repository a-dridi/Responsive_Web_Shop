/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Order;
import at.adridi.responsivewebshop.model.ProductOrder;
import at.adridi.responsivewebshop.model.dao.OrderDAO;
import at.adridi.responsivewebshop.model.dao.ProductOrderDAO;
import at.adridi.responsivewebshop.services.OrderStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

/**
 * Admin Page - Ordered Products
 *
 * @author A.Dridi
 */
@Named(value = "adminOrdersController")
@ViewScoped
public class AdminOrdersController implements Serializable {

    private OrderDAO orderDao = new OrderDAO();
    private ProductOrderDAO productOrderDao = new ProductOrderDAO();

    private List<Order> customerOrders;
    //Used to save value of a changed order status
    private Integer changedOrderStatusId;
    private List<OrderStatus> orderStatuesValues = OrderStatus.getAllOrderStatus();

    /**
     * Creates a new instance of AdminOrdersController
     */
    public AdminOrdersController() {
        this.customerOrders = this.orderDao.getAllOrderCreated();

    }

    public void changeOrderStatus(ValueChangeEvent event, int order_id) {
        int newOrderStatusId = (int) event.getNewValue();
        this.orderDao.updateOrderStatus(order_id, newOrderStatusId);
    }

    public List<ProductOrder> getProductsFromOrderId(int order_id) {
        return this.productOrderDao.getAllProductOrderByOrderId(order_id);
    }

    public List<Order> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<Order> customerOrders) {
        this.customerOrders = customerOrders;
    }

    public Integer getChangedOrderStatusId() {
        return changedOrderStatusId;
    }

    public void setChangedOrderStatusId(Integer changedOrderStatusId) {
        this.changedOrderStatusId = changedOrderStatusId;
    }

    public List<OrderStatus> getOrderStatuesValues() {
        return orderStatuesValues;
    }

    public void setOrderStatuesValues(List<OrderStatus> orderStatuesValues) {
        this.orderStatuesValues = orderStatuesValues;
    }

}
