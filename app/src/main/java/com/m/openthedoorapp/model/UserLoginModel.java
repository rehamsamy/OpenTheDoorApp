package com.m.openthedoorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("token")
    private String token;
    @SerializedName("user")
    private User user;
    @SerializedName("message")
    private String message;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getMessages() {
        return message;
    }

    // Class User ...
    public static class User {
        @SerializedName("id")
        private Integer id;
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("phone")
        private String phone;
        @SerializedName("user_image")
        private String user_image;

        // Getters
        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getUser_image() {
            return user_image;
        }
    }
}
