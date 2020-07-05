/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 * Admin Page - Manage Products. Edit or Delete existing products.
 *
 * @author A.Dridi
 */
@Named(value = "adminManageProducts")
@ViewScoped
public class AdminManageProducts implements Serializable{

    private ProductDAO productDao = new ProductDAO();
   // ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private List<Product> allProducts;

    /**
     * Creates a new instance of AdminManageProducts
     */
    public AdminManageProducts() {
        this.allProducts = this.productDao.getAllProduct();
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

    /**
     * Save changed values of Products.
     */
    public void saveAllProducts() {
        for (Product product : this.allProducts) {
            this.productDao.updateProduct(product);
        }
    }

}
