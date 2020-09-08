/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.responsivewebshop.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 *
 * @author A.Dridi
 */
@Entity
public class Settings implements Serializable{

    @Id
    private String settingkey;
    @Lob
    private String settingValue;

    public Settings(String settingkey, String settingValue) {
        this.settingkey = settingkey;
        this.settingValue = settingValue;
    }
    
    public Settings(){
        
    }
    
    public String getSettingkey() {
        return settingkey;
    }

    public void setSettingkey(String settingkey) {
        this.settingkey = settingkey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.settingkey);
        hash = 79 * hash + Objects.hashCode(this.settingValue);
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
        final Settings other = (Settings) obj;
        if (!Objects.equals(this.settingkey, other.settingkey)) {
            return false;
        }
        if (!Objects.equals(this.settingValue, other.settingValue)) {
            return false;
        }
        return true;
    }

    
}
