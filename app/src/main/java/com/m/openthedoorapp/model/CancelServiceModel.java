package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class CancelServiceModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("Userservicecancel")
    private UserService Userservicecancel;
    @SerializedName("messages")
    private String messages;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public UserService getUserservicecancel() {
        return Userservicecancel;
    }

    public String getMessages() {
        return messages;
    }

    // class UserService
    public static class UserService {
        @SerializedName("id")
        private Integer id;
        @SerializedName("user_service_id")
        private String user_service_id;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("reson_for_cancel")
        private String reson_for_cancel;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;

        // Getters...
        public Integer getId() {
            return id;
        }

        public String getUser_service_id() {
            return user_service_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getReson_for_cancel() {
            return reson_for_cancel;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
