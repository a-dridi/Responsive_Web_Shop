/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Class that has all app languages - Languages List
 *
 * @author A.Dridi
 */
public class Language implements Serializable {

    private String languageName;
    private Locale languageLocale;
    private String languageIsoCode;

    /**
     * All available languages - Languages List
     *
     * @return
     */
    public static List<Language> getAllLanguages() {
        List<Language> languagesList = new ArrayList<>();

        languagesList.add(new Language("English", Locale.ENGLISH, "en"));
        languagesList.add(new Language("German", Locale.GERMAN, "de"));

        return languagesList;
    }

    /**
     * Locale Language that is selected in this app. If not set, then load the
     * available language of browser client user. Default: English
     */
    public static void loadConfiguredLocale() {
        String userLanguageCode;
        try {
            userLanguageCode = (FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("accept-language")).split(",")[0];
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            userLanguageCode = "en";
        }
        int selectedLanguageIndex;
        try {
            selectedLanguageIndex = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("appLanguageIndex");
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Language.getAllLanguages().get(selectedLanguageIndex).getLanguageLocale());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            //Language selection not saved in session
            int languageIndex = 0;
            for (Language language : Language.getAllLanguages()) {
                if (language.getLanguageIsoCode().equals(userLanguageCode)) {
                    FacesContext.getCurrentInstance().getViewRoot().setLocale(language.getLanguageLocale());
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appLanguageIndex", languageIndex);
                    return;
                }
                languageIndex++;
            }
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appLanguageIndex", 0);
        }

    }

    public static void setConfiguredLocale(int selectedLanguageListIndex) {
        if (Language.getAllLanguages().get(selectedLanguageListIndex).getLanguageIsoCode() != null) {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Language.getAllLanguages().get(selectedLanguageListIndex).getLanguageLocale());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appLanguageIndex", selectedLanguageListIndex);
        } else {
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("appLanguageIndex", 0);
        }
    }

    public Language(String languageName, Locale languageLocale, String languageIsoCode) {
        this.languageName = languageName;
        this.languageLocale = languageLocale;
        this.languageIsoCode = languageIsoCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public Locale getLanguageLocale() {
        return languageLocale;
    }

    public void setLanguageLocale(Locale languageLocale) {
        this.languageLocale = languageLocale;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public Language() {

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.languageName);
        hash = 17 * hash + Objects.hashCode(this.languageIsoCode);
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
        final Language other = (Language) obj;
        if (!Objects.equals(this.languageName, other.languageName)) {
            return false;
        }
        if (!Objects.equals(this.languageIsoCode, other.languageIsoCode)) {
            return false;
        }
        return true;
    }

}
