package com.m.openthedoorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegsiterPhoneModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("code")
    private String code;
    @SerializedName("messages")
    private Errors messages;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Errors getMessages() {
        return messages;
    }


    // ------------ Error Class -------------
    public static class Errors {
        @SerializedName("phone")
        @Expose
        private List<String> phone = null;

        // Getters
        public List<String> getPhone() {
            return phone;
        }
    }
}
