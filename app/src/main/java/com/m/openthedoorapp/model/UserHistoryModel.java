package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserHistoryModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("userserviceinfo")
    private HistoryData userserviceinfo;


    // Getters
    public Boolean getStatus() {
        return status;
    }

    public HistoryData getUserserviceinfo() {
        return userserviceinfo;
    }


    // Class HistoryData ....
    public static class HistoryData {

        @SerializedName("current")
        private List<CurrentData> current;
        @SerializedName("inprocess")
        private List<InProgressData> inprocess;
        @SerializedName("completed")
        private List<CompletedData> completed;
        @SerializedName("canceled")
        private List<CanceledData> canceled;
        @SerializedName("scheduled")
        private List<ScheduledData> scheduled;


        // Getters
        public List<CurrentData> getCurrent() {
            return current;
        }

        public List<InProgressData> getInprocess() {
            return inprocess;
        }

        public List<CompletedData> getCompleted() {
            return completed;
        }

        public List<CanceledData> getCanceled() {
            return canceled;
        }

        public List<ScheduledData> getScheduled() {
            return scheduled;
        }
    }


    // class CurrentData
    public class CurrentData {

    }

    // class InProcessData
    public class InProgressData {

    }

    // class CompletedData
    public class CompletedData {

    }

    // class CanceledData
    public class CanceledData {

    }

    // class ScheduledData
    public class ScheduledData {

    }
}
