/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.controller;

import at.adridi.responsivewebshop.model.Settings;
import at.adridi.responsivewebshop.model.dao.SettingsDAO;
import at.adridi.responsivewebshop.services.Currencies;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 * Admin Page - Settings
 *
 * @author A.Dridi
 */
@Named(value = "adminSettingsController")
@ViewScoped
public class AdminSettingsController implements Serializable {

    private SettingsDAO settingsDao = new SettingsDAO();
    private String shopNameValue = "My Shop";
    private String adSpaceContentValue = "";
    private String selectedCurrencyValue = "USD";
    private String selectedCommaValue = "comma";
    private String privacyPolicyValue = "";
    private String legalNoticeValue = "";
    private String contactValue = "";
    ResourceBundle text = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "text");

    private List<String> currencyList = Currencies.currencyList;

    public AdminSettingsController() {
        try {
            this.shopNameValue = this.settingsDao.getSettingBySettingkey("shopName").getSettingValue();
            this.adSpaceContentValue = this.settingsDao.getSettingBySettingkey("adSpaceContent").getSettingValue();
            this.selectedCurrencyValue = this.settingsDao.getSettingBySettingkey("selectedCurrency").getSettingValue();
            this.selectedCommaValue = this.settingsDao.getSettingBySettingkey("floatNumberFormatting").getSettingValue();
            this.privacyPolicyValue = this.settingsDao.getSettingBySettingkey("privacyPolicy").getSettingValue();
            this.legalNoticeValue = this.settingsDao.getSettingBySettingkey("legalNotice").getSettingValue();
            this.contactValue = this.settingsDao.getSettingBySettingkey("contact").getSettingValue();
        } catch (NullPointerException e) {
            this.privacyPolicyValue = text.getString("privacypolicyPageTitle");
            this.legalNoticeValue = text.getString("legalnoticePageTitle");
            this.contactValue = text.getString("contactPageTitle");
        }
    }

    public void saveSettings() {
        Settings updateSetting = new Settings("shopName", this.shopNameValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("adSpaceContent", this.adSpaceContentValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("selectedCurrency", this.selectedCurrencyValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("floatNumberFormatting", this.selectedCommaValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("privacyPolicy", this.privacyPolicyValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("legalNotice", this.legalNoticeValue);
        this.settingsDao.updateSettings(updateSetting);
        updateSetting = new Settings("contact", this.contactValue);
        this.settingsDao.updateSettings(updateSetting);
    }

    public String getSelectedCommaValue() {
        return selectedCommaValue;
    }

    public void setSelectedCommaValue(String selectedCommaValue) {
        this.selectedCommaValue = selectedCommaValue;
    }

    public String getShopNameValue() {
        return shopNameValue;
    }

    public void setShopNameValue(String shopNameValue) {
        this.shopNameValue = shopNameValue;
    }

    public String getAdSpaceContentValue() {
        return adSpaceContentValue;
    }

    public void setAdSpaceContentValue(String adSpaceContentValue) {
        this.adSpaceContentValue = adSpaceContentValue;
    }

    public String getSelectedCurrencyValue() {
        return selectedCurrencyValue;
    }

    public void setSelectedCurrencyValue(String selectedCurrencyValue) {
        this.selectedCurrencyValue = selectedCurrencyValue;
    }

    public List<String> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<String> currencyList) {
        this.currencyList = currencyList;
    }

    public String getPrivacyPolicyValue() {
        return privacyPolicyValue;
    }

    public void setPrivacyPolicyValue(String privacyPolicyValue) {
        this.privacyPolicyValue = privacyPolicyValue;
    }

    public String getLegalNoticeValue() {
        return legalNoticeValue;
    }

    public void setLegalNoticeValue(String legalNoticeValue) {
        this.legalNoticeValue = legalNoticeValue;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.settingsDao);
        hash = 17 * hash + Objects.hashCode(this.shopNameValue);
        hash = 17 * hash + Objects.hashCode(this.adSpaceContentValue);
        hash = 17 * hash + Objects.hashCode(this.selectedCurrencyValue);
        hash = 17 * hash + Objects.hashCode(this.selectedCommaValue);
        hash = 17 * hash + Objects.hashCode(this.privacyPolicyValue);
        hash = 17 * hash + Objects.hashCode(this.legalNoticeValue);
        hash = 17 * hash + Objects.hashCode(this.contactValue);
        hash = 17 * hash + Objects.hashCode(this.text);
        hash = 17 * hash + Objects.hashCode(this.currencyList);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdminSettingsController other = (AdminSettingsController) obj;
        if (!Objects.equals(this.shopNameValue, other.shopNameValue)) {
            return false;
        }
        if (!Objects.equals(this.adSpaceContentValue, other.adSpaceContentValue)) {
            return false;
        }
        if (!Objects.equals(this.selectedCurrencyValue, other.selectedCurrencyValue)) {
            return false;
        }
        if (!Objects.equals(this.selectedCommaValue, other.selectedCommaValue)) {
            return false;
        }
        if (!Objects.equals(this.privacyPolicyValue, other.privacyPolicyValue)) {
            return false;
        }
        if (!Objects.equals(this.legalNoticeValue, other.legalNoticeValue)) {
            return false;
        }
        if (!Objects.equals(this.contactValue, other.contactValue)) {
            return false;
        }
        if (!Objects.equals(this.settingsDao, other.settingsDao)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.currencyList, other.currencyList)) {
            return false;
        }
        return true;
    }

    
    
}
