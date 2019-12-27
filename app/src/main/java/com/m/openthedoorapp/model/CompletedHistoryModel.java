package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletedHistoryModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("userserviceinfo")
    private CompletedHistoryData userserviceinfo;


    // Getters
    public Boolean getStatus() {
        return status;
    }

    public CompletedHistoryData getUserserviceinfo() {
        return userserviceinfo;
    }


    // Class HistoryData ....
    public static class CompletedHistoryData {

        @SerializedName("completed")
        private List<ServiceItemData> completed;

        // Getters

        public List<ServiceItemData> getCompleted() {
            return completed;
        }
    }


    // class CompletedData
//    public class CompletedData {
//        @SerializedName("id")
//        private Integer id;
//        @SerializedName("provider_price_per_hour")
//        private Integer provider_price_per_hour;
//        @SerializedName("provider_minutes_to_arrive")
//        private Integer provider_minutes_to_arrive;
//        @SerializedName("provider_hour_to_finish")
//        private Integer provider_hour_to_finish;
//        @SerializedName("cost_of_service_provider")
//        private Integer cost_of_service_provider;
//        @SerializedName("coupon_id")
//        private Integer coupon_id;
//        @SerializedName("coupon_value")
//        private String coupon_value;
//        @SerializedName("service_id")
//        private Integer service_id;
//        @SerializedName("total")
//        private Integer total;
//        @SerializedName("watch")
//        private Integer watch;
//        @SerializedName("user_id")
//        private Integer user_id;
//        @SerializedName("status")
//        private String status;
//        @SerializedName("provider_id")
//        private Integer provider_id;
//        @SerializedName("created_at")
//        private String created_at;
//        @SerializedName("updated_at")
//        private String updated_at;
//        @SerializedName("provider_name")
//        private String provider_name;
//        @SerializedName("user_name")
//        private String user_name;
//        @SerializedName("service_name_ar")
//        private String service_name_ar;
//        @SerializedName("service_name_en")
//        private String service_name_en;
//        @SerializedName("rat_count")
//        private Integer rat_count;
//        @SerializedName("provider_phone")
//        private String provider_phone;
//        @SerializedName("provider_email")
//        private String provider_email;
//        @SerializedName("provider_image")
//        private String provider_image;
//
//
//        // Getters...
//        public Integer getId() {
//            return id;
//        }
//
//        public Integer getProvider_price_per_hour() {
//            return provider_price_per_hour;
//        }
//
//        public Integer getProvider_minutes_to_arrive() {
//            return provider_minutes_to_arrive;
//        }
//
//        public Integer getProvider_hour_to_finish() {
//            return provider_hour_to_finish;
//        }
//
//        public Integer getCost_of_service_provider() {
//            return cost_of_service_provider;
//        }
//
//        public Integer getCoupon_id() {
//            return coupon_id;
//        }
//
//        public String getCoupon_value() {
//            return coupon_value;
//        }
//
//        public Integer getService_id() {
//            return service_id;
//        }
//
//        public Integer getTotal() {
//            return total;
//        }
//
//        public Integer getWatch() {
//            return watch;
//        }
//
//        public Integer getUser_id() {
//            return user_id;
//        }
//
//        public String getStatus() {
//            return status;
//        }
//
//        public Integer getProvider_id() {
//            return provider_id;
//        }
//
//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public String getUpdated_at() {
//            return updated_at;
//        }
//
//        public String getProvider_name() {
//            return provider_name;
//        }
//
//        public String getUser_name() {
//            return user_name;
//        }
//
//        public String getService_name_ar() {
//            return service_name_ar;
//        }
//
//        public String getService_name_en() {
//            return service_name_en;
//        }
//
//        public Integer getRat_count() {
//            return rat_count;
//        }
//
//        public String getProvider_phone() {
//            return provider_phone;
//        }
//
//        public String getProvider_email() {
//            return provider_email;
//        }
//
//        public String getProvider_image() {
//            return provider_image;
//        }
//    }
}
