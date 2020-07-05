/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author A.Dridi
 */
@Named(value = "logoutController")
@RequestScoped
public class LogoutController {

    /**
     * Creates a new instance of LogoutController
     */
    public LogoutController() {
    }
    
    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/faces/admin_login.xhtml?faces-redirect=true";
    }
    
}
