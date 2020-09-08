/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.ProductCategory;
import at.adridi.responsivewebshop.model.Settings;
import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.CheckoutProduct;
import at.adridi.responsivewebshop.services.Language;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author A.Dridi
 */
@Named(value = "mainShopPage")
@ViewScoped
public class MainShopPage implements Serializable {

    private SettingsDAO settingsDao = new SettingsDAO();
    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();
    private ProductDAO productDao = new ProductDAO();

    private List<ProductCategory> productCategories;
    private int productsViewerPosition = 0;
    private int numberOfDisplayedProducts = 20;
    private List<Product> newestProducts;
    private List<Product> allProducts;
    private String shopName = "My Shop";
    private boolean newestProductsAvailable = false;
    private boolean allProductsAvailable = false;
    private String adSpaceContent = "";
    private String checkoutButtonText = "";
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private List<Language> languagesList = Language.getAllLanguages();
    private Locale selectedLanguageIsoCode;
    private boolean adminUserSessionActive = false;
    private String selectedLanguageName = "English";
    private String companyName = "";
    private String contactInfo = "";

    /**
     * Creates a new instance of MainShopPage
     */
    public MainShopPage() {
        Language.loadConfiguredLocale();
        checkAdminAccountSession();
        insertDefaultDataToDatabase();
        this.productCategories = this.productCategoryDao.getAllProductCategory();
        this.newestProducts = this.productDao.getNLatestProductsFromPositionN(numberOfDisplayedProducts, productsViewerPosition);
        this.allProducts = this.productDao.getAllProduct();
        if (this.allProducts != null && this.allProducts.size() > 0) {
            this.allProductsAvailable = true;
        }
        if (this.newestProducts != null && this.newestProducts.size() > 0) {
            this.newestProductsAvailable = true;
        }
        if (this.settingsDao.getSettingBySettingkey("adSpaceContent") != null) {
            this.adSpaceContent = this.settingsDao.getSettingBySettingkey("adSpaceContent").getSettingValue();
        }
        if (this.settingsDao.getSettingBySettingkey("shopName") != null) {
            this.shopName = this.settingsDao.getSettingBySettingkey("shopName").getSettingValue();
        }
        if (this.settingsDao.getSettingBySettingkey("headerCompanyName") != null) {
            this.companyName = this.settingsDao.getSettingBySettingkey("headerCompanyName").getSettingValue();
        }
        if (this.settingsDao.getSettingBySettingkey("headerContactInfo") != null) {
            this.contactInfo = this.settingsDao.getSettingBySettingkey("headerContactInfo").getSettingValue();
        }
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
        this.selectedLanguageName = Language.getSelectedLanguageName();
    }

    public void changeSelectedLanguage(int selectedLanguageListIndex) {
        Language.setConfiguredLocale(selectedLanguageListIndex);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(MainShopPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkAdminAccountSession() {
        try {
            Boolean adminUserLoggedinState = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("adminuserloggedin");
            if (adminUserLoggedinState) {
                this.adminUserSessionActive = true;
            }
        } catch (NullPointerException e) {
            //this.adminUserSessionActive = false;
        }
    }

    public void insertDefaultDataToDatabase() {
        if (this.settingsDao.getSettingBySettingkey("shopName") == null) {
            Settings newSettings = new Settings("shopName", "My Shop");
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("adSpaceContent", "");
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("selectedCurrency", "USD");
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("privacyPolicy", text.getString("settingsPrivacyNoticeDesc"));
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("legalNotice", text.getString("settingsLegalNoticeDesc"));
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("contact", text.getString("settingsContactDesc"));
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("headerCompanyName", "Company Name");
            this.settingsDao.addSettings(newSettings);
            newSettings = new Settings("headerContactInfo", "📞 +00 123456789 - 📧 email@email.td");
            this.settingsDao.addSettings(newSettings);
        }
    }

    public void openCheckoutPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/checkout.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(MainShopPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public List<Language> getLanguagesList() {
        return languagesList;
    }

    public void setLanguagesList(List<Language> languagesList) {
        this.languagesList = languagesList;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public int getProductsViewerPosition() {
        return productsViewerPosition;
    }

    public void setProductsViewerPosition(int productsViewerPosition) {
        this.productsViewerPosition = productsViewerPosition;
    }

    public int getNumberOfDisplayedProducts() {
        return numberOfDisplayedProducts;
    }

    public void setNumberOfDisplayedProducts(int numberOfDisplayedProducts) {
        this.numberOfDisplayedProducts = numberOfDisplayedProducts;
    }

    public List<Product> getNewestProducts() {
        return newestProducts;
    }

    public void setNewestProducts(List<Product> newestProducts) {
        this.newestProducts = newestProducts;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<Product> allProducts) {
        this.allProducts = allProducts;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isNewestProductsAvailable() {
        return newestProductsAvailable;
    }

    public void setNewestProductsAvailable(boolean newestProductsAvailable) {
        this.newestProductsAvailable = newestProductsAvailable;
    }

    public boolean isAllProductsAvailable() {
        return allProductsAvailable;
    }

    public void setAllProductsAvailable(boolean allProductsAvailable) {
        this.allProductsAvailable = allProductsAvailable;
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

    public Locale getSelectedLanguageIsoCode() {
        return selectedLanguageIsoCode;
    }

    public void setSelectedLanguageIsoCode(Locale selectedLanguageIsoCode) {
        this.selectedLanguageIsoCode = selectedLanguageIsoCode;
    }

    public boolean isAdminUserSessionActive() {
        return adminUserSessionActive;
    }

    public void setAdminUserSessionActive(boolean adminUserSessionActive) {
        this.adminUserSessionActive = adminUserSessionActive;
    }

    public String getSelectedLanguageName() {
        return selectedLanguageName;
    }

    public void setSelectedLanguageName(String selectedLanguageName) {
        this.selectedLanguageName = selectedLanguageName;
    }

}
