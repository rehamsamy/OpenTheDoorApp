package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class SetPasswordModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("messages")
    private String message;
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private UserSomeData user;


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
        return user;
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
