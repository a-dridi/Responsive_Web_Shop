/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Customer;
import at.adridi.responsivewebshop.model.dao.CustomerDAO;
import at.adridi.responsivewebshop.model.dao.OrderDAO;
import at.adridi.responsivewebshop.services.PaypalCredentials;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Capturing Successfull (Confirmed and Paid) PayPal Order Transaction. Order
 * will be made availanle in Database. *
 *
 * @author A.Dridi
 */
@Named(value = "orderSuccess")
@ViewScoped
public class OrderSuccess implements Serializable {

    @Inject
    private HttpServletRequest request;
    private OrderDAO orderDao = new OrderDAO();
    private CustomerDAO customerDao = new CustomerDAO();

    private String orderNumber;
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

    /**
     * Make unconfiremed order to a confirmed order. Update customer information
     * or create new customer from PayPal data.
     */
    public OrderSuccess() {
        String uri = request.getRequestURI();
        try {
            Integer orderNumber = Integer.parseInt((uri.split("=")[1]));
            at.adridi.responsivewebshop.model.Order confirmedOrder = this.orderDao.getOrderById(orderNumber);
            OrdersCaptureRequest request = new OrdersCaptureRequest(confirmedOrder.getPaypalOrderId());
            request.requestBody(new OrderRequest());
            PaypalCredentials paypalClient = new PaypalCredentials();
            HttpResponse<Order> response = paypalClient.getClient().execute(request);
            confirmedOrder.setOrderStatus(1);

            Customer customer = this.customerDao.getCustomerByEmail(response.result().payer().email());
            if (customer == null) {
                Customer newCustomer = new Customer();
                newCustomer.setEmail(response.result().payer().email());
                newCustomer.setForename(response.result().payer().name().givenName());
                newCustomer.setSurname(response.result().payer().name().surname());
                newCustomer.setStreet(response.result().payer().addressPortable().addressLine1() + " " + response.result().payer().addressPortable().addressLine2());
                try {
                    newCustomer.setPostcode(Integer.parseInt(response.result().payer().addressPortable().postalCode()));
                } catch (NumberFormatException | NullPointerException e) {
                    newCustomer.setPostcode(0);
                }
                newCustomer.setCounty(response.result().payer().addressPortable().addressLine3());
                newCustomer.setCountry(response.result().payer().addressPortable().countryCode());
                newCustomer.setDescription(response.result().payer().taxInfo().taxId());
                this.customerDao.addCustomer(newCustomer);
                confirmedOrder.setCustomer(newCustomer);

            } else {
                customer.setForename(response.result().payer().name().givenName());
                customer.setSurname(response.result().payer().name().surname());
                customer.setStreet(response.result().payer().addressPortable().addressLine1() + " " + response.result().payer().addressPortable().addressLine2());
                try {
                    customer.setPostcode(Integer.parseInt(response.result().payer().addressPortable().postalCode()));
                } catch (NumberFormatException | NullPointerException e) {
                    customer.setPostcode(0);
                }
                customer.setCounty(response.result().payer().addressPortable().addressLine3());
                customer.setCountry(response.result().payer().addressPortable().countryCode());
                customer.setDescription(response.result().payer().taxInfo().taxId());
                confirmedOrder.setCustomer(customer);
                this.customerDao.updateCustomer(customer);
            }
            confirmedOrder.setCustomer(customer);
            confirmedOrder.setCreated(true);
            this.orderDao.updateOrder(confirmedOrder);
            this.orderNumber = "" + confirmedOrder.getPaypalOrderId();
        } catch (NullPointerException | NumberFormatException | IOException e) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(OrderSuccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
