package com.example.dataobjects;

/**
 * Created by peekay on 9/2/17.
 */

public class CurrencyTypes {
    String currencyCode;// title
    String description;// description
    boolean active;
    String currencySymbol; //currencySymbol
    String mobileNumberCount;
    String countryCode;
    String sysCountryCode;
    String countryName;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getMobileNumberCount() {
        return mobileNumberCount;
    }

    public void setMobileNumberCount(String mobileNumberCount) {
        this.mobileNumberCount = mobileNumberCount;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSysCountryCode() {
        return sysCountryCode;
    }

    public void setSysCountryCode(String sysCountryCode) {
        this.sysCountryCode = sysCountryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrencySymbolConverted() {
        String result = "";
        int step = 6;
        for(int i = 0; i < currencySymbol.length()/6; i++)
        {
            result +=  (char) Integer.parseInt( currencySymbol.substring( 2 + step * i,  2 + step * i + 4), 16 );
        }
        return result;
    }

}

