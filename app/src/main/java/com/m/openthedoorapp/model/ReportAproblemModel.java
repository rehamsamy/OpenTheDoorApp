package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class ReportAproblemModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("Reportproblem")
    private ProblemData Reportproblem;
    @SerializedName("messages")
    private String messages;

    public Boolean getStatus() {
        return status;
    }

    public ProblemData getReportproblem() {
        return Reportproblem;
    }

    public String getMessages() {
        return messages;
    }

    // class Reportproblem
    public static class ProblemData {
        @SerializedName("id")
        private Integer id;
        @SerializedName("user_id")
        private Integer user_id;
        @SerializedName("problem")
        private String problem;
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

        public String getProblem() {
            return problem;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }
    }
}
