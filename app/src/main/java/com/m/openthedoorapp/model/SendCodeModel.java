package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class SendCodeModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("user_id")
    private String user_id;

    // Getters...
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_id() {
        return user_id;
    }
}
