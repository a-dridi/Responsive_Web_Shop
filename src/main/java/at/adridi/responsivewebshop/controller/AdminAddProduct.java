/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.constants.ProductPicture;
import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.ProductCategory;
import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 * Admin Page - Add new Product
 *
 * @author A.Dridi
 */
@Named(value = "adminAddProduct")
@ViewScoped
public class AdminAddProduct implements Serializable {

    private SettingsDAO settingsDao = new SettingsDAO();
    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();
    private ProductDAO productDao = new ProductDAO();

    private String activeCurrency;
    private String productTitle;
    private String productDescription;
    private String priceCurrencyUnit;
    private String priceCentUnit;
    private String taxRate;
    private ProductCategory productCategory;
    private Integer availableAmount;
    private boolean available = true;
    private UploadedFile productPhotoFile;
    private List<ProductCategory> productCategories;
    private String commaSeperator; //Comma or point used for seperation of cent value. 
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

    /**
     * Creates a new instance of AdminAddProduct
     */
    public AdminAddProduct() {
        //Format price (which is in cent) to double value with comma or points for comma values according to the settings. 
        try {
            if (this.settingsDao.getSettingBySettingkey("floatNumberFormatting").getSettingValue().equals("comma")) {
                this.commaSeperator = ",";
            } else {
                //US style format with point instead of comma (ex.: 22.24)
                this.commaSeperator = ".";
            }
        } catch (NullPointerException e) {
            this.commaSeperator = ".";
        }
        this.productCategories = this.productCategoryDao.getAllProductCategory();
        this.activeCurrency = this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue();
    }

    public void addProduct() {
        System.out.println("ADD NEW PRODUCT");
        Product newProduct = new Product();
        newProduct.setTitle(this.productTitle);
        newProduct.setDescription(this.productDescription);
        int parsedCentPrice = 0;
        try {
            parsedCentPrice = (int) (Double.parseDouble(this.priceCurrencyUnit + "." + this.priceCentUnit) * 100);
        } catch (NullPointerException | NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage("addProductDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorEnterCorrectPrice"), null));
            return;
        }
        newProduct.setPriceCents(parsedCentPrice);
        try {
            int taxRate = Integer.parseInt(this.taxRate);
            newProduct.setTax(taxRate);
        } catch (NullPointerException | NumberFormatException e) {
            newProduct.setTax(0);
        }
        newProduct.setCategory(this.productCategory);
        newProduct.setAvailableAmount(this.availableAmount);
        newProduct.setAvailable(this.available);
        Path productsPhotoFolder = Paths.get(ProductPicture.PRODUCT_PICTURE_PATH);
        String productphotoFilename = "";
        String productphotoExtension = "";

        if (this.productPhotoFile != null) {
            productphotoFilename = FilenameUtils.getBaseName(this.productPhotoFile.getFileName());
            productphotoExtension = FilenameUtils.getExtension(this.productPhotoFile.getFileName());
            try {
                newProduct.setProductPhotoPath(ProductPicture.PRODUCT_PICTURE_PATH.concat(productphotoFilename).concat(".").concat(productphotoExtension));
            } catch (NullPointerException e) {
                this.productPhotoFile = null;
            }
        } 

        if (this.productDao.addProduct(newProduct)) {
            //Save uploaded product picture
            if (this.productPhotoFile != null) {
                try ( InputStream productPhotoInput = this.productPhotoFile.getInputStream()) {
                    Path productphotoFile = Files.createTempFile(productsPhotoFolder, productphotoFilename, "." + productphotoExtension);
                    Files.copy(productPhotoInput, productphotoFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(EditProduct.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("PRODUCT OK ADDED");
                FacesContext.getCurrentInstance().addMessage("addProductDisplay", new FacesMessage(FacesMessage.SEVERITY_INFO, text.getString("okProductSaved"), null));
            }
        } else {
            System.out.println("PRODUCT FAIL ADDED");
            FacesContext.getCurrentInstance().addMessage("addProductDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorProductSaved"), null));
        }
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getActiveCurrency() {
        return activeCurrency;
    }

    public void setActiveCurrency(String activeCurrency) {
        this.activeCurrency = activeCurrency;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPriceCurrencyUnit() {
        return priceCurrencyUnit;
    }

    public void setPriceCurrencyUnit(String priceCurrencyUnit) {
        this.priceCurrencyUnit = priceCurrencyUnit;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public UploadedFile getProductPhotoFile() {
        return productPhotoFile;
    }

    public void setProductPhotoFile(UploadedFile productPhotoFile) {
        this.productPhotoFile = productPhotoFile;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public String getCommaSeperator() {
        return commaSeperator;
    }

    public void setCommaSeperator(String commaSeperator) {
        this.commaSeperator = commaSeperator;
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public String getPriceCentUnit() {
        return priceCentUnit;
    }

    public void setPriceCentUnit(String priceCentUnit) {
        this.priceCentUnit = priceCentUnit;
    }

}
