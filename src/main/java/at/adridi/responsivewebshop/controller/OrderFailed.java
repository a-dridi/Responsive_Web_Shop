/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Order;
import at.adridi.responsivewebshop.model.dao.OrderDAO;
import at.adridi.responsivewebshop.services.CheckoutProduct;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * PayPal Transaction was canceled by Customer. Order will be deleted from
 * Database.
 *
 * @author A.Dridi
 */
@Named(value = "orderFailed")
@ViewScoped
public class OrderFailed implements Serializable {

    @Inject
    private HttpServletRequest request;

    private String checkoutButtonText = "";
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private OrderDAO orderDao = new OrderDAO();

    /**
     * Creates a new instance of OrderFailed
     */
    public OrderFailed() {
        //Load items in checkout and create checkout button text
        List<CheckoutProduct> addedProductsAmounts;
        try {
            addedProductsAmounts = (List<CheckoutProduct>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shoppingCart");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            addedProductsAmounts = new ArrayList<>();
        }
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();

        //Delete unconfirmed order from Database
        String uri = request.getRequestURI();
        try {
            Integer orderNumber = Integer.parseInt((uri.split("=")[1]));
            Order canceledOrder = this.orderDao.getOrderById(orderNumber);
            if (canceledOrder == null) {
                throw new NullPointerException();
            }
            canceledOrder.setOrderStatus(4);
            this.orderDao.updateOrder(canceledOrder);
        } catch (NullPointerException | NumberFormatException e) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(OrderSuccess.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void openCheckoutPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/checkout.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(MainShopPage.class.getName()).log(Level.SEVERE, null, ex);
        }
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

}
