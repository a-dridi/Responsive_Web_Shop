/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Product;
import at.adridi.responsivewebshop.model.ProductOrder;
import at.adridi.responsivewebshop.model.dao.OrderDAO;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.CheckoutProduct;
import at.adridi.responsivewebshop.services.PaypalCredentials;
import at.adridi.responsivewebshop.services.ShoppingCart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author A.Dridi
 */
@Named(value = "checkout")
@ViewScoped
public class Checkout implements Serializable {

    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");
    private String checkoutButtonText = "";
    private List<CheckoutProduct> addedProductsAmounts;
    private String shopName = "My Shop";
    private SettingsDAO settingsDao = new SettingsDAO();
    private OrderDAO orderDao = new OrderDAO();

    private boolean shoppingcartEmpty = false;
    private String activeCurrency;
    private int netOrderSum = 0;
    private int fullOrderSum = 0;
    private int vatOrderSum = 0;

    private int orderNewNumber = 1;

    at.adridi.responsivewebshop.model.Order newUnconfirmedOrder = new at.adridi.responsivewebshop.model.Order();

    public Checkout() {
        try {
            this.activeCurrency = this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue();
        } catch (NullPointerException e) {
            this.activeCurrency = "USD";
        }

        if (this.settingsDao.getSettingBySettingkey("shopName") != null) {
            this.shopName = this.settingsDao.getSettingBySettingkey("shopName").getSettingValue();
        }

        //Load items in checkout 
        try {
            addedProductsAmounts = (List<CheckoutProduct>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("shoppingCart");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            addedProductsAmounts = new ArrayList<>();
        }
        if (addedProductsAmounts != null && addedProductsAmounts.size() > 0) {
            this.shoppingcartEmpty = false;
        } else {
            this.shoppingcartEmpty = true;
        }
        //Create Order sums (net sum, vat tax sum and full sum)
        if (this.addedProductsAmounts != null) {
            for (CheckoutProduct checkoutProduct : this.addedProductsAmounts) {
                this.netOrderSum += checkoutProduct.getAddedProduct().getPriceCents();
                this.vatOrderSum += checkoutProduct.getAddedProduct().getVATTaxValue();
                this.fullOrderSum += (checkoutProduct.getAddedProduct().getPriceCents() + checkoutProduct.getAddedProduct().getVATTaxValue());
            }
            List<at.adridi.responsivewebshop.model.Order> allDoneOrders = this.orderDao.getAllOrder();
            if (allDoneOrders != null) {
                orderNewNumber = (allDoneOrders.size()) + 1;
            }
        }
        ShoppingCart currentShoppingCartState = new ShoppingCart();
        this.checkoutButtonText = currentShoppingCartState.getShoppingCartStatusString();
    }

    public void removeProduct(CheckoutProduct removedCheckoutProduct) {
        this.addedProductsAmounts.remove(removedCheckoutProduct);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("shoppingCart", addedProductsAmounts);
    }

    /**
     * Create POST Request to "Paypal" Payment Gateway "/v2/checkout/orders".
     * User is then redirected to the "Paypal" payment page. User can then
     * confirm and pay the order through "Paypal". PaypalCredentials
     */
    public void doCheckout() {
        try {
            OrdersCreateRequest request = new OrdersCreateRequest();
            request.header("prefer", "return=representation");
            request.requestBody(createOrderRequest());
            PaypalCredentials credentials = new PaypalCredentials();
            HttpResponse<Order> response;
            response = credentials.getClient().execute(request);
            this.newUnconfirmedOrder.setPaypalOrderId(response.result().id());
            this.newUnconfirmedOrder.setSumCent(this.fullOrderSum);
            this.newUnconfirmedOrder.setCurrency(this.activeCurrency);
            this.orderDao.addOrder(this.newUnconfirmedOrder);
            FacesContext.getCurrentInstance().getExternalContext().redirect(response.result().links().get(1).href());

        } catch (IOException ex) {
            Logger.getLogger(Checkout.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Creates an OrderRequest to the payment gateway "Paypal": Add all ordered
     * products to the request. User is redirect to the payment process in
     * Paypal. If the payment was successfull, then the order is processed and
     * user is redirected to "order_success.xhtml". If payment failed, then the user is redirect to the site "order_canceled.xhtml". 
     *
     * @return
     */
    public OrderRequest createOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext().brandName(this.shopName)
                .landingPage("BILLING")
                .cancelUrl(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/order_canceled.xhtml?ord=" + this.orderNewNumber)
                .returnUrl(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/order_success.xhtml?ord=" + this.orderNewNumber)
                .userAction("PAY_NOW")
                .paymentToken(shopName)
                .shippingPreference("GET_FROM_FILE");
        orderRequest.applicationContext(applicationContext);

        //Add ordered products to the "Paypal" purchase order. 
        Set<ProductOrder> orderedProducts = new HashSet<>();
        List<PurchaseUnitRequest> purchaseUnitRequestsList = new ArrayList<PurchaseUnitRequest>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().referenceId("PUHF")
                .description(text.getString("orderPaypalDescription") + " " + this.orderNewNumber)
                .customId("" + this.orderNewNumber)
                .softDescriptor("" + this.orderNewNumber)
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode(this.activeCurrency).value("" + (this.fullOrderSum) / 100)
                        .amountBreakdown(new AmountBreakdown().itemTotal(new Money().currencyCode(this.activeCurrency).value(String.valueOf(this.netOrderSum)))
                                .taxTotal(new Money().currencyCode(this.activeCurrency).value("" + this.vatOrderSum))))
                .items(new ArrayList<Item>() {
                    {
                        for (CheckoutProduct checkoutProduct : addedProductsAmounts) {
                            ProductOrder orderedProductItem = new ProductOrder();
                            orderedProductItem.setAmount(checkoutProduct.getAddedAmount());
                            orderedProductItem.setProduct(checkoutProduct.getAddedProduct());
                            orderedProductItem.setPriceCent(checkoutProduct.getAddedProduct().getPriceCents());
                            orderedProductItem.setOrderOfProductItem(newUnconfirmedOrder);
                            orderedProducts.add(orderedProductItem);

                            add(new Item().name(checkoutProduct.getAddedProduct().getTitle())
                                    .description(checkoutProduct.getAddedProduct().getCategory().getCategoryName())
                                    .sku("" + checkoutProduct.getAddedProduct().getProduct_id())
                                    .unitAmount(new Money().currencyCode(activeCurrency).value("" + (checkoutProduct.getAddedProduct().getPriceCents() / 100)))
                                    .tax(new Money().currencyCode(activeCurrency).value("" + (checkoutProduct.getAddedProduct().getVATTaxValue() / 100)))
                                    .quantity("" + checkoutProduct.getAddedAmount())
                                    .category(checkoutProduct.getAddedProduct().isDownloadableProdut() ? "DIGITAL_GOODS" : "PHYSICAL_GOODS")
                            );

                        }
                    }
                });
        purchaseUnitRequestsList.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequestsList);
        this.newUnconfirmedOrder.setOrderedProducts(orderedProducts);
        return orderRequest;
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

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }

    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }

    public List<CheckoutProduct> getAddedProductsAmounts() {
        return addedProductsAmounts;
    }

    public void setAddedProductsAmounts(List<CheckoutProduct> addedProductsAmounts) {
        this.addedProductsAmounts = addedProductsAmounts;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isShoppingcartEmpty() {
        return shoppingcartEmpty;
    }

    public void setShoppingcartEmpty(boolean shoppingcartEmpty) {
        this.shoppingcartEmpty = shoppingcartEmpty;
    }

    public int getNetOrderSum() {
        return netOrderSum;
    }

    public void setNetOrderSum(int netOrderSum) {
        this.netOrderSum = netOrderSum;
    }

    public int getFullOrderSum() {
        return fullOrderSum;
    }

    public void setFullOrderSum(int fullOrderSum) {
        this.fullOrderSum = fullOrderSum;
    }

    public int getVatOrderSum() {
        return vatOrderSum;
    }

    public void setVatOrderSum(int vatOrderSum) {
        this.vatOrderSum = vatOrderSum;
    }

}
