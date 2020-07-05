/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.dao.ProductCategoryDAO;
import at.adridi.responsivewebshop.model.dao.ProductDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.FilenameUtils;

/**
 *
 * @author A.Dridi
 */
@Named(value = "editProduct")
@Dependent
public class EditProduct {

    @Inject
    private HttpServletRequest httpServletRequest;
    private Product editedProduct = new Product();
    private SettingsDAO settingsDao = new SettingsDAO();
    private ProductDAO productDao = new ProductDAO();
    private ProductCategoryDAO productCategoryDao = new ProductCategoryDAO();

    private String activeCurrency;
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private List<String> productCategories;
    private UploadedFile productPhotoFile;
    private String netPriceInfo;

    public EditProduct() {

        this.httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String uri = httpServletRequest.getRequestURI();
        try {
            Integer productIdParsed = Integer.parseInt(uri.split("=")[1]);
            this.editedProduct = this.productDao.getProductById(productIdParsed);
            if (this.editedProduct == null) {
                throw new ArrayIndexOutOfBoundsException("Product does not exist!");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/responsiveWebShop/");
                return;
            } catch (IOException ex) {
                Logger.getLogger(ViewProduct.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.activeCurrency = text.getString("currencyText") + " (" + this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue() + ")";
        this.productCategories = this.productCategoryDao.getAllProductCategoryName();

        this.netPriceInfo = text.getString("netpriceInfoText") + this.activeCurrency;
    }

    /**
     * Save product in database and save product photo (replace existing)
     */
    public void saveProductAndProductPhoto() {
        this.productDao.updateProduct(editedProduct);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/responsiveWebShop/faces/admin_manage.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(EditProduct.class.getName()).log(Level.SEVERE, null, ex);
        }

        Path productsPhotoFolder = Paths.get("/images/productsphoto/");
        String productphotoFilename = FilenameUtils.getBaseName(this.productPhotoFile.getFileName());
        String productphotoExtension = FilenameUtils.getExtension(this.productPhotoFile.getFileName());
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

    public List<String> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<String> productCategories) {
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

}
