package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class AddReviewResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("SuccessMsg")
    private String SuccessMsg;
    @SerializedName("Review")
    private ReviewContent Review;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public String getSuccessMsg() {
        return SuccessMsg;
    }

    public ReviewContent getReview() {
        return Review;
    }


    // Class ReviewContent
    public static class ReviewContent{
        @SerializedName("id")
        private int id;
        @SerializedName("notes")
        private String notes;
        @SerializedName("rate")
        private String rate;
        @SerializedName("provider_id")
        private String provider_id;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;

        // Getter
        public int getId() {
            return id;
        }

        public String getNotes() {
            return notes;
        }

        public String getRate() {
            return rate;
        }

        public String getProvider_id() {
            return provider_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
