/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * An ordered product connected with Order and Product
 *
 * @author A.Dridi
 */
@Entity
@SequenceGenerator(name = "productorder_seq", sequenceName = "productorder_id_seq", allocationSize = 1)
public class ProductOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "productorder_seq")
    private Integer productorder_id;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="productor_order_id")
    private Order orderOfProductItem;
    
    @OneToOne(optional=false)
    @JoinColumn(name="product_productord_id")
    private Product product;
    
    private Integer priceCent;
    private Integer amount;

    public Integer getProductorder_id() {
        return productorder_id;
    }

    public void setProductorder_id(Integer productorder_id) {
        this.productorder_id = productorder_id;
    }

    public Order getOrderOfProductItem() {
        return orderOfProductItem;
    }

    public void setOrderOfProductItem(Order orderOfProductItem) {
        this.orderOfProductItem = orderOfProductItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getPriceCent() {
        return priceCent;
    }

    public void setPriceCent(Integer priceCent) {
        this.priceCent = priceCent;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.productorder_id);
        hash = 83 * hash + Objects.hashCode(this.orderOfProductItem);
        hash = 83 * hash + Objects.hashCode(this.product);
        hash = 83 * hash + Objects.hashCode(this.priceCent);
        hash = 83 * hash + Objects.hashCode(this.amount);
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
        final ProductOrder other = (ProductOrder) obj;
        if (!Objects.equals(this.productorder_id, other.productorder_id)) {
            return false;
        }
        if (!Objects.equals(this.orderOfProductItem, other.orderOfProductItem)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        if (!Objects.equals(this.priceCent, other.priceCent)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        return true;
    }

  
    
}
