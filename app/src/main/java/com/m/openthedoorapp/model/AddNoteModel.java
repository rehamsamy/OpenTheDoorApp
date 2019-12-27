package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class AddNoteModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("providernotes")
    private NoteData providernotes;
    @SerializedName("messages")
    private String messages;

    public Boolean getStatus() {
        return status;
    }

    public NoteData getProvidernotes() {
        return providernotes;
    }

    public String getMessages() {
        return messages;
    }

    // class Reportproblem
    public static class NoteData {
        @SerializedName("id")
        private Integer id;
        @SerializedName("provider_id")
        private Integer provider_id;
        @SerializedName("user_id")
        private Integer user_id;
        @SerializedName("notes")
        private String notes;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;

        //Getters
        public Integer getId() {
            return id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public Integer getProvider_id() {
            return provider_id;
        }

        public String getNotes() {
            return notes;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
