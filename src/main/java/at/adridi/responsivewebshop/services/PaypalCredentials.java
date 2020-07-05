/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 * Load Paypal SDK Credentials
 */
public class PaypalCredentials {

    private String clientId = "";
    private String secret = "";
    private PayPalEnvironment environment;
    private PayPalHttpClient client;
    public ResourceBundle credentialsData = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "credentials");

    public PaypalCredentials() {
        this.clientId = this.credentialsData.getString("paypalClientid");
        this.secret = this.credentialsData.getString("paypalSecret");
        this.environment = new PayPalEnvironment.Sandbox(this.clientId, this.secret);
        this.client = new PayPalHttpClient(this.environment);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public PayPalEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(PayPalEnvironment environment) {
        this.environment = environment;
    }

    public PayPalHttpClient getClient() {
        return client;
    }

    public void setClient(PayPalHttpClient client) {
        this.client = client;
    }
    
    

}
