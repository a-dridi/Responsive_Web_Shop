/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Product. Net price will be used to create full price automatically with set
 * tax
 *
 * @author A.Dridi
 */
@Entity
@SequenceGenerator(name = "product_seq", sequenceName = "product_id_seq", allocationSize = 1)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "product_seq")
    private Integer product_id;
    @NotNull
    private String title;
    //Net price
    private Integer priceCents;
    private Integer tax = 0;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;
    @Lob
    private String description;
    private String productPhotoPath;
    //Product that can be downloaded - true: if this is not a physical product
    private boolean downloadableProdut = false;
    private Integer availableAmount;
    private boolean available;

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get Net price
     *
     * @return
     */
    public Integer getPriceCents() {
        return priceCents;
    }

    /**
     * Set Net price
     *
     * @return
     */
    public void setPriceCents(Integer priceCents) {
        this.priceCents = priceCents;

    }
    
    public Integer getVATTaxValue(){
        return (this.priceCents*(this.tax/100));
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductPhotoPath() {
        return productPhotoPath;
    }

    public void setProductPhotoPath(String productPhotoPath) {
        this.productPhotoPath = productPhotoPath;
    }

    public boolean isDownloadableProdut() {
        return downloadableProdut;
    }

    public void setDownloadableProdut(boolean downloadableProdut) {
        this.downloadableProdut = downloadableProdut;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.product_id);
        hash = 97 * hash + Objects.hashCode(this.title);
        hash = 97 * hash + Objects.hashCode(this.priceCents);
        hash = 97 * hash + Objects.hashCode(this.category);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.productPhotoPath);
        hash = 97 * hash + (this.downloadableProdut ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.availableAmount);
        hash = 97 * hash + (this.available ? 1 : 0);
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
        final Product other = (Product) obj;
        if (this.downloadableProdut != other.downloadableProdut) {
            return false;
        }
        if (this.available != other.available) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.productPhotoPath, other.productPhotoPath)) {
            return false;
        }
        if (!Objects.equals(this.product_id, other.product_id)) {
            return false;
        }
        if (!Objects.equals(this.priceCents, other.priceCents)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.availableAmount, other.availableAmount)) {
            return false;
        }
        return true;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    /**
     * Generate price inclusive vat (tax rate)
     *
     * @return
     */
    public Integer getFullPriceCents() {
        return (this.priceCents) + ((this.priceCents * this.tax) / 100);
    }

}
