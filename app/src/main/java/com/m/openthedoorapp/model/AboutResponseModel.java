package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class AboutResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("aboutus")
    private AboutData aboutus;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public AboutData getAboutus() {
        return aboutus;
    }

    // Class About Data
    public static class AboutData{
        @SerializedName("content_ar")
        private String content_ar;
        @SerializedName("content_en")
        private String content_en;

        // Getter
        public String getContent_ar() {
            return content_ar;
        }

        public String getContent_en() {
            return content_en;
        }
    }
}
