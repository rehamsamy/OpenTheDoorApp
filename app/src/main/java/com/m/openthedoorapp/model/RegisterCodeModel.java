package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class RegisterCodeModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;
    @SerializedName("userinfo")
    private UserSomeData userinfo;


    // Getter..
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UserSomeData getUserinfo() {
        return userinfo;
    }


    // class UserSomeData
    public static class UserSomeData{
        @SerializedName("id")
        private Integer id;
        @SerializedName("phone")
        private String phone;

        // Getters
        public Integer getId() {
            return id;
        }

        public String getPhone() {
            return phone;
        }
    }
}
