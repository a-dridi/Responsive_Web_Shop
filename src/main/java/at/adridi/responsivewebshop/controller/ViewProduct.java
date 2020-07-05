/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.CheckoutProduct;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author A.Dridi
 */
@Named(value = "viewProduct")
@Dependent
public class ViewProduct {

    private SettingsDAO settingsDao = new SettingsDAO();
    private ProductDAO productDao = new ProductDAO();

    @Inject
    private HttpServletRequest httpServletRequest;
    private Product openedProduct = new Product();
    private int addedAmount = 1;
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private String productPriceDisplayed;
    private String activeCurrency;
    private String adSpaceContent = "";
    private String checkoutButtonText = "";
    private String tvaInfo;
    private boolean productDoesNotExist = false;

    /**
     * Load product (price formatted according to settings, selected currency,
     * etc.) if product id was passed through URI. If that is not the case, then
     * redirect to the home page.
     */
    public ViewProduct() {
        this.httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uri = httpServletRequest.getRequestURI();
        Integer productIdParsed = Integer.parseInt(httpServletRequest.getParameter("product_id"));

        this.openedProduct = this.productDao.getProductById(productIdParsed);
        if (this.openedProduct == null) {
            this.productDoesNotExist = true;
            return;
        }

        try {
            this.activeCurrency = this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue();
        } catch (NullPointerException e) {
            this.activeCurrency = "USD";
        }

        //Format price (which is in cent) to double value with comma or points for comma values according to the settings. 
        if (this.settingsDao.getSettingBySettingkey("floatNumberFormatting") != null && this.settingsDao.getSettingBySettingkey("floatNumberFormatting").getSettingValue().equals("comma")) {
            this.productPriceDisplayed = String.format(Locale.GERMAN, "%.2f", (this.openedProduct.getPriceCents() / 100));
        } else {
            //US style format with point instead of comma (ex.: 22.24)
            this.productPriceDisplayed = String.format(Locale.US, "%.2f", (this.openedProduct.getPriceCents() / 100));
        }
        if (this.settingsDao.getSettingBySettingkey("adSpaceContent") != null) {
            this.adSpaceContent = this.settingsDao.getSettingBySettingkey("adSpaceContent").getSettingValue();
        }

        //TVA info
        this.tvaInfo = "(" + this.openedProduct.getTax() + " %" + this.text.getString("tvaInfoText") + ")";

        //Load items in checkout and create checkout button text
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
    }

    /**
     * Ads a map with the added product object and amount to the session storage
     * for the current shopping cart.
     */
    public void addProductToCard() {
        List<CheckoutProduct> addedProductsAmounts;
        try {
            addedProductsAmounts = (List<CheckoutProduct>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shoppingCart");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            addedProductsAmounts = new ArrayList<>();
        }
        CheckoutProduct checkoutProduct = new CheckoutProduct(this.openedProduct, this.addedAmount);
        addedProductsAmounts.add(checkoutProduct);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("shoppingCart", addedProductsAmounts);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, text.getString("productedAddedToCartSuccess"), null));
    }

    public Product getOpenedProduct() {
        return openedProduct;
    }

    public void setOpenedProduct(Product openedProduct) {
        this.openedProduct = openedProduct;
    }

    public int getAddedAmount() {
        return addedAmount;
    }

    public void setAddedAmount(int addedAmount) {
        this.addedAmount = addedAmount;
    }

    public String getProductPriceDisplayed() {
        return productPriceDisplayed;
    }

    public void setProductPriceDisplayed(String productPriceDisplayed) {
        this.productPriceDisplayed = productPriceDisplayed;
    }

    public String getActiveCurrency() {
        return activeCurrency;
    }

    public void setActiveCurrency(String activeCurrency) {
        this.activeCurrency = activeCurrency;
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

    public String getTvaInfo() {
        return tvaInfo;
    }

    public void setTvaInfo(String tvaInfo) {
        this.tvaInfo = tvaInfo;
    }

    public boolean isProductDoesNotExist() {
        return productDoesNotExist;
    }

    public void setProductDoesNotExist(boolean productDoesNotExist) {
        this.productDoesNotExist = productDoesNotExist;
    }
    
    
}
