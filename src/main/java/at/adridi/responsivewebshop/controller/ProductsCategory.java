/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.ProductCategory;
import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Products of a category.
 *
 * @author A.Dridi
 */
@Named(value = "productsCategory")
@Dependent
public class ProductsCategory {

    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();
    private ProductDAO productDao = new ProductDAO();
    private SettingsDAO settingsDao = new SettingsDAO();

    private List<Product> productsOfCategory;
    @Inject
    private HttpServletRequest httpServletRequest;
    private ProductCategory selectedProductCategory;
    private String adSpaceContent = "";
    private String checkoutButtonText = "";
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    @Inject
    private ExternalContext externalContext;
    private boolean categoryDoesNotExist = false;

    /**
     * Creates a new instance of ProductsCategory
     */
    public ProductsCategory() {
        this.httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {
            Integer categoryIdParsed = Integer.parseInt(httpServletRequest.getParameter("category"));
            this.productsOfCategory = this.productDao.getProductByProductCategory(categoryIdParsed);
            this.selectedProductCategory = this.productCategoryDao.getProductCategoryById(categoryIdParsed);
            if (this.selectedProductCategory == null) {
                throw new ArrayIndexOutOfBoundsException("Product category does not exist!");
            }

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            this.categoryDoesNotExist = true;
        }

        if (this.settingsDao.getSettingBySettingkey("adSpaceContent") != null) {
            this.adSpaceContent = this.settingsDao.getSettingBySettingkey("adSpaceContent").getSettingValue();
        }

        //Load items in checkout and create checkout button text
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
    }

    public boolean isCategoryDoesNotExist() {
        return categoryDoesNotExist;
    }

    public void setCategoryDoesNotExist(boolean categoryDoesNotExist) {
        this.categoryDoesNotExist = categoryDoesNotExist;
    }

    public List<Product> getProductsOfCategory() {
        return productsOfCategory;
    }

    public void setProductsOfCategory(List<Product> productsOfCategory) {
        this.productsOfCategory = productsOfCategory;
    }

    public ProductCategory getSelectedProductCategory() {
        return selectedProductCategory;
    }

    public void setSelectedProductCategory(ProductCategory selectedProductCategory) {
        this.selectedProductCategory = selectedProductCategory;
    }

    public String getAdSpaceContent() {
        return adSpaceContent;
    }

    public void setAdSpaceContent(String adSpaceContent) {
        this.adSpaceContent = adSpaceContent;
    }

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }

    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }
}
