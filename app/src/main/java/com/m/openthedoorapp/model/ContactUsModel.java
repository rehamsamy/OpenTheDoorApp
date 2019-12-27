package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class ContactUsModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("appinfo")
    private ContactData appinfo;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public ContactData getAppinfo() {
        return appinfo;
    }


    // class ContactData
    public static class ContactData {

        @SerializedName("website_phone")
        private String website_phone;
        @SerializedName("website_email")
        private String website_email;
        @SerializedName("website_address_ar")
        private String website_address_ar;
        @SerializedName("website_address_en")
        private String website_address_en;
        @SerializedName("website_url")
        private String website_url;

        // Getters
        public String getWebsite_phone() {
            return website_phone;
        }

        public String getWebsite_email() {
            return website_email;
        }

        public String getWebsite_address_ar() {
            return website_address_ar;
        }

        public String getWebsite_address_en() {
            return website_address_en;
        }

        public String getWebsite_url() {
            return website_url;
        }
    }
}
