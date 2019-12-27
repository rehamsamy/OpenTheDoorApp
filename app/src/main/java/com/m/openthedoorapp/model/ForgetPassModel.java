package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class ForgetPassModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("MobileConfCode")
    private String MobileConfCode;

    //Getters...
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getMobileConfCode() {
        return MobileConfCode;
    }
}
