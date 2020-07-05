/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 * Has the localized (translated) descriptions for the status code of the table
 * column orderStatus from the table order.
 *
 * @author A.Dridi
 */
public class OrderStatus implements Serializable {

    private int statusCode;
    private String description;

    public OrderStatus(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;

    }

    /**
     * Get the localized (translated) descriptions for the passed status code
     */
    public static String getOrderStatusDescription(int statusCode) {
        ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

        switch (statusCode) {
            case 1:
                return text.getString("orderStatusCode1");
            case 2:
                return text.getString("orderStatusCode2");
            case 3:
                return text.getString("orderStatusCode3");
            case 4:
                return text.getString("orderStatusCode4");
            default:
                return "N/A";
        }

    }

    public static List<OrderStatus> getAllOrderStatus() {
        ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
        List<OrderStatus> allOrderStatusObjects = new ArrayList<>();
        allOrderStatusObjects.add(new OrderStatus(1, text.getString("orderStatusCode1")));
        allOrderStatusObjects.add(new OrderStatus(2, text.getString("orderStatusCode2")));
        allOrderStatusObjects.add(new OrderStatus(3, text.getString("orderStatusCode3")));
        allOrderStatusObjects.add(new OrderStatus(4, text.getString("orderStatusCode4")));
        return allOrderStatusObjects;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.statusCode;
        hash = 53 * hash + Objects.hashCode(this.description);
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
        final OrderStatus other = (OrderStatus) obj;
        if (this.statusCode != other.statusCode) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
    
    
    
}
