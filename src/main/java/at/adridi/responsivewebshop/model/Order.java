/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model;

import at.adridi.responsivewebshop.services.OrderStatus;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * A done order by customer. All finished orders from "UnconfirmedOrder".
 *
 * @author A.dridi
 */
@Entity
@SequenceGenerator(name = "order_seq", sequenceName = "order_id_seq", allocationSize = 1, initialValue = 1000)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "order_seq")
    private Integer order_id;

    @OneToOne(optional=false)
    @JoinColumn(name="order_customer_id")
    private Customer customer;
    
    private Integer sumCent;
    
    private String paypalOrderId;
    private String currency = "USD";
    //Order is confirmed - not canceled
    private boolean created = false;

    @OneToMany(mappedBy = "orderOfProductItem")
    private Set<ProductOrder> orderedProducts = new HashSet<>();

    /**
     * Order Status - Check class: services -> orderstatus
     * 0 - Order is in process to be confirmed and paid by user
     * 1 - Order Processing
     * 2 - Order Sent
     * 3 - Order Returned
     * 4 - Order Cancelled
     */
    private Integer orderStatus = 1;

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getSumCent() {
        return sumCent;
    }

    public void setSumCent(Integer sumCent) {
        this.sumCent = sumCent;
    }

    public Set<ProductOrder> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<ProductOrder> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }

    /**
     * Order is confirmed. But not canceled.
     * @return 
     */
    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    
    
    @Override
    public String toString() {
        StringBuilder orderString = new StringBuilder("#");
        orderString.append(this.paypalOrderId);
        orderString.append(" - ");
        orderString.append(OrderStatus.getOrderStatusDescription(this.orderStatus));
        return orderString.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.order_id);
        hash = 31 * hash + Objects.hashCode(this.customer);
        hash = 31 * hash + Objects.hashCode(this.sumCent);
        hash = 31 * hash + Objects.hashCode(this.paypalOrderId);
        hash = 31 * hash + Objects.hashCode(this.currency);
        hash = 31 * hash + (this.created ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.orderedProducts);
        hash = 31 * hash + Objects.hashCode(this.orderStatus);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.created != other.created) {
            return false;
        }
        if (!Objects.equals(this.paypalOrderId, other.paypalOrderId)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.order_id, other.order_id)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        if (!Objects.equals(this.sumCent, other.sumCent)) {
            return false;
        }
        if (!Objects.equals(this.orderedProducts, other.orderedProducts)) {
            return false;
        }
        if (!Objects.equals(this.orderStatus, other.orderStatus)) {
            return false;
        }
        return true;
    }
 
    
    
}
