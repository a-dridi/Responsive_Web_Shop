/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * Shopping Cart - Load information - Number of added product items in the
 * shopping cart
 *
 * @author A.Dridi
 */
public class ShoppingCart {

    public ShoppingCart(){
        
    }
    
    /**
     * Get String with number of added product items in the shopping cart
     *
     * @return
     */
    public String getShoppingCartStatusString() {
        ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
        String checkoutButtonText;
        //Load items in checkout and create checkout button text
        List<CheckoutProduct> addedProductsAmounts;
        try {
            addedProductsAmounts = (List<CheckoutProduct>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shoppingCart");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            addedProductsAmounts = new ArrayList<>();
        }
        StringBuilder createCheckOutButtonText;
        if (addedProductsAmounts != null && addedProductsAmounts.size() > 0) {
            createCheckOutButtonText = new StringBuilder("(");
            createCheckOutButtonText.append(addedProductsAmounts.size());
            createCheckOutButtonText.append(") ");
            createCheckOutButtonText.append(text.getString("checkoutButtonText"));
            checkoutButtonText = createCheckOutButtonText.toString();
        } else {
            checkoutButtonText = text.getString("checkoutButtonText");
        }
        return checkoutButtonText;
    }
}
