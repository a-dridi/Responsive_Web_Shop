/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import static javax.security.enterprise.AuthenticationStatus.*;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 *
 * @author A.Dridi
 */
@Named(value = "loginController")
@ViewScoped
public class LoginController implements Serializable{

    @NotEmpty
    @Email(message = "{emailNotValidLogin}")
    private String email;

    @NotEmpty
    @Size(min = 6, message = "{passwordNotValidLogin}")
    private String password;

    @Inject
    private SecurityContext loginSecurityContext;

    @Inject
    private ExternalContext loginExternalContext;

    @Inject
    private FacesContext loginFacesContext;

    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

    private boolean adminaccountCreated;

    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
        this.loginFacesContext = FacesContext.getCurrentInstance();
        SettingsDAO settingsDao = new SettingsDAO();
        try {
            String adminEmail = settingsDao.getSettingBySettingkey("credentialsAdminEmail").getSettingValue();
            if (!adminEmail.equals("")) {
                this.adminaccountCreated = true;
            } else {
                this.adminaccountCreated = false;
            }
        } catch (NullPointerException e) {
            this.adminaccountCreated = false;
        }
    }

    public void login() {
        switch (doAuthentication()) {
            case SEND_CONTINUE:
                this.loginFacesContext.responseComplete();
                break;
            case SEND_FAILURE:
                this.loginFacesContext.addMessage("loginStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorLoginFailed"), null));
                break;
            case SUCCESS:
                this.loginFacesContext.addMessage("loginStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_INFO, text.getString("errorLoginSuccessful"), null));
                try {
                    this.loginExternalContext.redirect(this.loginExternalContext.getRequestContextPath() + "/faces/admin_orders.xhtml");
                } catch (IOException e) {
                    this.loginFacesContext.addMessage("loginStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorRedirectLoginFailed"), e.getMessage()));
                    System.out.println("" + e);
                }
        }

    }

    private AuthenticationStatus doAuthentication() {
        return loginSecurityContext.authenticate((HttpServletRequest) loginExternalContext.getRequest(), (HttpServletResponse) loginExternalContext.getResponse(), AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(email, password)));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isAdminaccountCreated() {
        return adminaccountCreated;
    }

    public void setAdminaccountCreated(boolean adminaccountCreated) {
        this.adminaccountCreated = adminaccountCreated;
    }

}
