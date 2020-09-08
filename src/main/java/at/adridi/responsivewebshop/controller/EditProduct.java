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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author A.Dridi
 */
@Named(value = "editProduct")
@ViewScoped
public class EditProduct implements Serializable {

    @Inject
    private HttpServletRequest httpServletRequest;
    private Product editedProduct = new Product();
    private SettingsDAO settingsDao = new SettingsDAO();
    private ProductDAO productDao = new ProductDAO();
    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();

    private String activeCurrency;
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private List<ProductCategory> productCategories;

    private UploadedFile productPhotoFile;
    private String netPriceInfo;
    private boolean productDoesNotExist = false;
    private String commaSeperator; //Comma or point used for seperation of cent value. 
    private String priceCurrencyUnit; //Integer currency value (USD; Euro, etc.)
    private String priceCentUnit; //Comma currency value (Pennies, Cents, etc.)

    public EditProduct() {
        this.httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Integer productIdParsed = Integer.parseInt(httpServletRequest.getParameter("product_id"));
        this.editedProduct = this.productDao.getProductById(productIdParsed);
        if (this.editedProduct == null) {
            this.productDoesNotExist = true;
        }

        this.activeCurrency = " (" + this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue() + ")";
        this.productCategories = this.productCategoryDao.getAllProductCategory();
        this.netPriceInfo = text.getString("netpriceInfoText") + this.activeCurrency;

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

        //Parse price integer values (unit: usd, euro) and comma values (pennies, cent) from price value of the database. 
        this.priceCurrencyUnit = String.valueOf(((int) (this.editedProduct.getPriceCents() / 100)));
        this.priceCentUnit = String.valueOf(((int) (this.editedProduct.getPriceCents() % 100)));
    }

    /**
     * Save product in database and save product photo (replace existing)
     */
    public void saveProductAndProductPhoto() {
        int parsedCentPrice;
        try {
            parsedCentPrice = (int) (Double.parseDouble(this.priceCurrencyUnit + "." + this.priceCentUnit) * 100);
        } catch (NullPointerException | NumberFormatException e) {
            FacesContext.getCurrentInstance().addMessage("editProductsForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorEnterCorrectPrice"), null));
            return;
        }
        editedProduct.setPriceCents(parsedCentPrice);

        Path productsPhotoFolder = Paths.get(ProductPicture.PRODUCT_PICTURE_PATH);
        String productphotoFilename = "";
        String productphotoExtension = "";
        if (this.productPhotoFile != null) {
            productphotoFilename = FilenameUtils.getBaseName(this.productPhotoFile.getFileName());
            productphotoExtension = FilenameUtils.getExtension(this.productPhotoFile.getFileName());

            try {
                editedProduct.setProductPhotoPath(ProductPicture.PRODUCT_PICTURE_PATH.concat(productphotoFilename).concat(".").concat(productphotoExtension));
            } catch (NullPointerException e) {
                editedProduct.setProductPhotoPath("");
            }
        } else {
            editedProduct.setProductPhotoPath("");
        }

        this.productDao.updateProduct(editedProduct);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/responsiveWebShop/faces/admin_manage.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EditProduct.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Save upload product picture
        try ( InputStream productPhotoInput = this.productPhotoFile.getInputStream()) {
            Path productphotoFile = Files.createTempFile(productsPhotoFolder, productphotoFilename, "." + productphotoExtension);
            Files.copy(productPhotoInput, productphotoFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Logger.getLogger(EditProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancel() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/responsiveWebShop/faces/admin_manage.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EditProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Product getEditedProduct() {
        return editedProduct;
    }

    public void setEditedProduct(Product editedProduct) {
        this.editedProduct = editedProduct;
    }

    public List<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public UploadedFile getProductPhotoFile() {
        return productPhotoFile;
    }

    public void setProductPhotoFile(UploadedFile productPhotoFile) {
        this.productPhotoFile = productPhotoFile;
    }

    public String getActiveCurrency() {
        return activeCurrency;
    }

    public void setActiveCurrency(String activeCurrency) {
        this.activeCurrency = activeCurrency;
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public String getNetPriceInfo() {
        return netPriceInfo;
    }

    public void setNetPriceInfo(String netPriceInfo) {
        this.netPriceInfo = netPriceInfo;
    }

    public boolean isProductDoesNotExist() {
        return productDoesNotExist;
    }

    public void setProductDoesNotExist(boolean productDoesNotExist) {
        this.productDoesNotExist = productDoesNotExist;
    }

    public String getCommaSeperator() {
        return commaSeperator;
    }

    public void setCommaSeperator(String commaSeperator) {
        this.commaSeperator = commaSeperator;
    }

    public String getPriceCurrencyUnit() {
        return priceCurrencyUnit;
    }

    public void setPriceCurrencyUnit(String priceCurrencyUnit) {
        this.priceCurrencyUnit = priceCurrencyUnit;
    }

    public String getPriceCentUnit() {
        return priceCentUnit;
    }

    public void setPriceCentUnit(String priceCentUnit) {
        this.priceCentUnit = priceCentUnit;
    }

}
