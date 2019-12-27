package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("services")
    private List<ServiceItem> services;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public List<ServiceItem> getServices() {
        return services;
    }


    // Class ServiceItem
    public static class ServiceItem{
        @SerializedName("id")
        private Integer id;
        @SerializedName("service_name_ar")
        private String service_name_ar;
        @SerializedName("service_name_en")
        private String service_name_en;

        // Getters
        public Integer getId() {
            return id;
        }

        public String getService_name_ar() {
            return service_name_ar;
        }

        public String getService_name_en() {
            return service_name_en;
        }
    }
}
