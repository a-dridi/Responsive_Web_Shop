/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import at.adridi.responsivewebshop.model.Product;
import java.util.Objects;

/**
 * Saves an added product in the shopping cart. Product object and Integer
 * "amount".
 *
 * @author ardat
 */
public class CheckoutProduct {

    private Product addedProduct;
    private Integer addedAmount;

    public CheckoutProduct(Product addedProduct, Integer addedAmount) {
        this.addedProduct = addedProduct;
        this.addedAmount = addedAmount;
    }

    public CheckoutProduct() {

    }

    public Product getAddedProduct() {
        return addedProduct;
    }

    public void setAddedProduct(Product addedProduct) {
        this.addedProduct = addedProduct;
    }

    public Integer getAddedAmount() {
        return addedAmount;
    }

    public void setAddedAmount(Integer addedAmount) {
        this.addedAmount = addedAmount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.addedProduct);
        hash = 17 * hash + Objects.hashCode(this.addedAmount);
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
        final CheckoutProduct other = (CheckoutProduct) obj;
        if (!Objects.equals(this.addedProduct, other.addedProduct)) {
            return false;
        }
        if (!Objects.equals(this.addedAmount, other.addedAmount)) {
            return false;
        }
        return true;
    }

}
