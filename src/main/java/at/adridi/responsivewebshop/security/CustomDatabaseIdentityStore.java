/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.security;

import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.SecurePasswordCreation;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * @author A.Dridi
 */
@ApplicationScoped
public class CustomDatabaseIdentityStore implements IdentityStore {

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential adminLoginCredentials = (UsernamePasswordCredential) credential;
        String dbAdminEmail = "";
        String dbAdminPassword = "";
        SettingsDAO settingsDao = new SettingsDAO();
        try {
            dbAdminEmail = settingsDao.getSettingBySettingkey("credentialsAdminEmail").getSettingValue();
            dbAdminPassword = settingsDao.getSettingBySettingkey("credentialsAdminPassword").getSettingValue();
            SecurePasswordCreation createHashedPassword = new SecurePasswordCreation();
            String enteredPasswordHashed = "";
            enteredPasswordHashed = createHashedPassword.getHashedMD5Password(adminLoginCredentials.getPasswordAsString(), createHashedPassword.getSalt());

            if (adminLoginCredentials.getCaller().equals(dbAdminEmail) && enteredPasswordHashed.equals(dbAdminPassword)) {
                System.out.println("Admin Login in SUCCESS");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("adminuserloggedin", true);
                return new CredentialValidationResult("admin", new HashSet<>(Arrays.asList("ADMIN")));
            } else {
                System.out.println("Admin Login in failed");
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            //Wrong Username and/or password was entered
            System.out.println("NOT_VALIDATED_RESULT");
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }

}
