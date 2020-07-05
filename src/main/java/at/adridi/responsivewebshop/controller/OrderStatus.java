/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Order;
import at.adridi.responsivewebshop.model.ProductOrder;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 * Customers can view the status of a certain order by filling their email and
 * order number in a form.
 *
 * @author A.Dridi
 */
@Named(value = "orderStatus")
@ViewScoped
public class OrderStatus implements Serializable {

    private OrderDAO orderDao = new OrderDAO();
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private String email = "";
    private String orderNumber = "";
    private Order selectedOrderByUser = new Order();
    private boolean orderStatusLoaded = false;
    private String checkoutButtonText = "";
    private List<ProductOrder> selectedOrderedProducts;
    @Inject
    private FacesContext orderstatusFacesContext;

    public OrderStatus() {
        //Load items in checkout and create checkout button text
        List<CheckoutProduct> addedProductsAmounts;
        try {
            addedProductsAmounts = (List<CheckoutProduct>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shoppingCart");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            addedProductsAmounts = new ArrayList<>();
        }
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
    }

    public void loadStatus() {
        System.out.println("RUN");

        int enteredOrderNumber;
        if (this.email == null || this.email.equals("")) {
            this.orderstatusFacesContext.addMessage("orderstatusErrorDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorEnterEmail"), null));
            return;
        }
        if (this.orderNumber == null || this.orderNumber.equals("")) {
            this.orderstatusFacesContext.addMessage("orderstatusErrorDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorEnterOrderNumber"), null));
            return;
        }
        try {
            enteredOrderNumber = Integer.parseInt(this.orderNumber);
        } catch (NumberFormatException e) {
            this.orderstatusFacesContext.addMessage("orderstatusErrorDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorEnterOrderNumber"), null));
            return;
        }
        Order orderOfOrderId = this.orderDao.getOrderByPaypalId(enteredOrderNumber);
        if (orderOfOrderId == null) {
            this.orderstatusFacesContext.addMessage("orderstatusErrorDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorOrderNotFound"), null));
            return;
        }
        if (!(orderOfOrderId.getCustomer().getEmail()).equals(this.email)) {
            this.orderstatusFacesContext.addMessage("orderstatusErrorDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorOrderNotFound"), null));
            return;
        }
        //Order which is linked to the entered email and order number exists 
        this.selectedOrderByUser = orderOfOrderId;
        this.selectedOrderByUser.getOrderedProducts();
        this.selectedOrderedProducts = new ArrayList<>();
        for (ProductOrder productOrderitem : this.selectedOrderByUser.getOrderedProducts()) {
            this.selectedOrderedProducts.add(productOrderitem);
        }
        this.orderStatusLoaded = true;

    }

    public void openCheckoutPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/checkout.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(MainShopPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order getSelectedOrderByUser() {
        return selectedOrderByUser;
    }

    public void setSelectedOrderByUser(Order selectedOrderByUser) {
        this.selectedOrderByUser = selectedOrderByUser;
    }

    public boolean isOrderStatusLoaded() {
        return orderStatusLoaded;
    }

    public void setOrderStatusLoaded(boolean orderStatusLoaded) {
        this.orderStatusLoaded = orderStatusLoaded;
    }

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }

    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }

    public List<ProductOrder> getSelectedOrderedProducts() {
        return selectedOrderedProducts;
    }

    public void setSelectedOrderedProducts(List<ProductOrder> selectedOrderedProducts) {
        this.selectedOrderedProducts = selectedOrderedProducts;
    }

}
