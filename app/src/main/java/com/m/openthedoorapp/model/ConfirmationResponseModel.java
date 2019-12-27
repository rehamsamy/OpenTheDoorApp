package com.m.openthedoorapp.model;

import com.google.gson.annotations.SerializedName;

public class ConfirmationResponseModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("userserviceinfo")
    private ServiceData userserviceinfo;

    //Getters
    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ServiceData getUserserviceinfo() {
        return userserviceinfo;
    }


    // class ServiceData
    public static class ServiceData {
        @SerializedName("id")
        private Integer id;
        @SerializedName("provider_price_per_hour")
        private Integer provider_price_per_hour;
        @SerializedName("provider_minutes_to_arrive")
        private Integer provider_minutes_to_arrive;
        @SerializedName("provider_hour_to_finish")
        private Integer provider_hour_to_finish;
        @SerializedName("bounce")
        private String bounce;
        @SerializedName("cost_of_service_provider")
        private Integer cost_of_service_provider;
        @SerializedName("coupon_id")
        private Integer coupon_id;
        @SerializedName("service_id")
        private Integer service_id;
        @SerializedName("total")
        private Integer total;
        @SerializedName("user_id")
        private Integer user_id;
        @SerializedName("status")
        private String status;
        @SerializedName("payment_method")
        private String payment_method;
        @SerializedName("provider_id")
        private Integer provider_id;
        @SerializedName("schedule_time")
        private String schedule_time;
        @SerializedName("notes")
        private String notes;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("provider_name")
        private String provider_name;
        @SerializedName("user_name")
        private String user_name;
        @SerializedName("service_name_ar")
        private String service_name_ar;
        @SerializedName("service_name_en")
        private String service_name_en;

        //Getters
        public Integer getId() {
            return id;
        }

        public Integer getProvider_price_per_hour() {
            return provider_price_per_hour;
        }

        public Integer getProvider_minutes_to_arrive() {
            return provider_minutes_to_arrive;
        }

        public Integer getProvider_hour_to_finish() {
            return provider_hour_to_finish;
        }

        public String getBounce() {
            return bounce;
        }

        public Integer getCost_of_service_provider() {
            return cost_of_service_provider;
        }

        public Integer getCoupon_id() {
            return coupon_id;
        }

        public Integer getService_id() {
            return service_id;
        }

        public Integer getTotal() {
            return total;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public String getStatus() {
            return status;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public Integer getProvider_id() {
            return provider_id;
        }

        public String getSchedule_time() {
            return schedule_time;
        }

        public String getNotes() {
            return notes;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getProvider_name() {
            return provider_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getService_name_ar() {
            return service_name_ar;
        }

        public String getService_name_en() {
            return service_name_en;
        }
    }
}
