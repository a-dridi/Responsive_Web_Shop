/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 * Admin Page - Add new Product Category
 *
 * @author A.Dridi
 */
@Named(value = "adminAddProductCategory")
@ViewScoped
public class AdminAddProductCategory implements Serializable {

    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private String addproductCategory = "";

    public AdminAddProductCategory() {
    }

    public void saveProductCategory() {
        if (!this.addproductCategory.trim().equals("")) {
            if (this.productCategoryDao.insertProductCategory(addproductCategory)) {
                FacesContext.getCurrentInstance().addMessage("addProductCategoryDisplay", new FacesMessage(FacesMessage.SEVERITY_INFO, text.getString("adminAddProductsCategorySuccess"), null));
            } else {
                FacesContext.getCurrentInstance().addMessage("addProductCategoryDisplay", new FacesMessage(FacesMessage.SEVERITY_FATAL, text.getString("adminAddProductsCategoryErrorDB"), null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("addProductCategoryDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("adminAddProductsCategoryError"), null));
        }
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public String getAddproductCategory() {
        return addproductCategory;
    }

    public void setAddproductCategory(String addproductCategory) {
        this.addproductCategory = addproductCategory;
    }

}
