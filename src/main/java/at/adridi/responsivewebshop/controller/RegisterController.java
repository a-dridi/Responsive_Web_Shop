/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Settings;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.SecurePasswordCreation;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

/**
 * Admin Account Registration
 *
 * @author A.Dridi
 */
@Named(value = "registerController")
@ViewScoped
public class RegisterController implements Serializable {

    private SettingsDAO settingsDao = new SettingsDAO();
    private String email;
    private String password;
    private String passwordConfirmation;
    private boolean adminaccountNotCreated;

    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

    /**
     * Creates a new instance of RegisterController
     */
    public RegisterController() {
        try {
            String adminEmail = this.settingsDao.getSettingBySettingkey("credentialsAdminEmail").getSettingValue();
            if (adminEmail.equals("")) {
                this.adminaccountNotCreated = true;
            } else {
                this.adminaccountNotCreated = false;
            }
        } catch (NullPointerException e) {
            this.adminaccountNotCreated = true;
        }

    }

    public void register() {
        if (this.email == null || this.email.equals("")) {
            FacesContext.getCurrentInstance().addMessage("registerStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorRegisterEmailEmpty"), null));
            return;
        }
        if (this.password == null || this.password.equals("")) {
            FacesContext.getCurrentInstance().addMessage("registerStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorRegisterPasswordEmpty"), null));
            return;
        }

        if (this.passwordConfirmation != null && this.password.equals(this.passwordConfirmation)) {
            Settings newAdminAccount = new Settings();
            newAdminAccount.setSettingkey("credentialsAdminEmail");
            newAdminAccount.setSettingValue(this.email);
            this.settingsDao.addSettings(newAdminAccount);
            newAdminAccount = new Settings();
            newAdminAccount.setSettingkey("credentialsAdminPassword");
            SecurePasswordCreation createHashedPassword = new SecurePasswordCreation();
            byte[] adminAccountSalt = createHashedPassword.getSalt();
            newAdminAccount.setSettingValue(createHashedPassword.getHashedMD5Password(this.password, adminAccountSalt));
            this.settingsDao.addSettings(newAdminAccount);
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/admin_login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
            FacesContext.getCurrentInstance().addMessage("registerStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_INFO, text.getString("successAdminAccountCreated"), null));

        } else {
            FacesContext.getCurrentInstance().addMessage("registerStatusDisplay", new FacesMessage(FacesMessage.SEVERITY_ERROR, text.getString("errorRegisterPasswordNotSame"), null));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public boolean isAdminaccountNotCreated() {
        return adminaccountNotCreated;
    }

    public void setAdminaccountNotCreated(boolean adminaccountNotCreated) {
        this.adminaccountNotCreated = adminaccountNotCreated;
    }

    public ResourceBundle getText() {
        return text;
    }

    public void setText(ResourceBundle text) {
        this.text = text;
    }

}
