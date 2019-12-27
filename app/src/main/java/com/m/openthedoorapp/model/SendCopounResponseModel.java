package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class SendCopounResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
