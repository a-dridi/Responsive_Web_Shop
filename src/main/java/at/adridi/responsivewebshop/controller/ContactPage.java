/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.CheckoutProduct;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;

/**
 * Footer Pages
 *
 * @author A.Dridi
 */
@Named(value = "contactPage")
@Dependent
public class ContactPage {

    private SettingsDAO settingsDao = new SettingsDAO();
    private String checkoutButtonText = "";
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private String shopName = "Shop Name";
    private String contactPageContent = "";

    public ContactPage() {
        if (this.settingsDao.getSettingBySettingkey("shopName") != null) {
            this.shopName = this.settingsDao.getSettingBySettingkey("shopName").getSettingValue();
        }
        if (this.settingsDao.getSettingBySettingkey("contact") != null) {
            this.contactPageContent = this.settingsDao.getSettingBySettingkey("contact").getSettingValue();
        } else {
            this.contactPageContent = text.getString("contactPageTitle");
        }

        //Load items in checkout and create checkout button text
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
    }

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }

    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getContactPageContent() {
        return contactPageContent;
    }

    public void setContactPageContent(String contactPageContent) {
        this.contactPageContent = contactPageContent;
    }

}
