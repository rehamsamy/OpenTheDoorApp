package com.m.openthedoorapp.model;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InProcessHistoryModel {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("userserviceinfo")
    private InprogressHistoryData userserviceinfo;


    // Getters
    public Boolean getStatus() {
        return status;
    }

    public InprogressHistoryData getUserserviceinfo() {
        return userserviceinfo;
    }


    // Class HistoryData ....
    public static class InprogressHistoryData {

        @SerializedName("inprocess")
        private List<ServiceItemData> inprocess;


        // Getters
        public List<ServiceItemData> getInprocess() {
            return inprocess;
        }
    }


    // class InProcessData
//    public class InProcessData implements Parcelable {
//
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
//        protected InProcessData(Parcel in) {
//            if (in.readByte() == 0) {
//                id = null;
//            } else {
//                id = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                provider_price_per_hour = null;
//            } else {
//                provider_price_per_hour = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                provider_minutes_to_arrive = null;
//            } else {
//                provider_minutes_to_arrive = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                provider_hour_to_finish = null;
//            } else {
//                provider_hour_to_finish = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                cost_of_service_provider = null;
//            } else {
//                cost_of_service_provider = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                coupon_id = null;
//            } else {
//                coupon_id = in.readInt();
//            }
//            coupon_value = in.readString();
//            if (in.readByte() == 0) {
//                service_id = null;
//            } else {
//                service_id = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                total = null;
//            } else {
//                total = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                watch = null;
//            } else {
//                watch = in.readInt();
//            }
//            if (in.readByte() == 0) {
//                user_id = null;
//            } else {
//                user_id = in.readInt();
//            }
//            status = in.readString();
//            if (in.readByte() == 0) {
//                provider_id = null;
//            } else {
//                provider_id = in.readInt();
//            }
//            created_at = in.readString();
//            updated_at = in.readString();
//            provider_name = in.readString();
//            user_name = in.readString();
//            service_name_ar = in.readString();
//            service_name_en = in.readString();
//            if (in.readByte() == 0) {
//                rat_count = null;
//            } else {
//                rat_count = in.readInt();
//            }
//        }
//
//        public final Creator<InProcessData> CREATOR = new Creator<InProcessData>() {
//            @Override
//            public InProcessData createFromParcel(Parcel in) {
//                return new InProcessData(in);
//            }
//
//            @Override
//            public InProcessData[] newArray(int size) {
//                return new InProcessData[size];
//            }
//        };
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
//
//        public Creator<InProcessData> getCREATOR() {
//            return CREATOR;
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            if (id == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(id);
//            }
//            if (provider_price_per_hour == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(provider_price_per_hour);
//            }
//            if (provider_minutes_to_arrive == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(provider_minutes_to_arrive);
//            }
//            if (provider_hour_to_finish == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(provider_hour_to_finish);
//            }
//            if (cost_of_service_provider == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(cost_of_service_provider);
//            }
//            if (coupon_id == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(coupon_id);
//            }
//            dest.writeString(coupon_value);
//            if (service_id == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(service_id);
//            }
//            if (total == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(total);
//            }
//            if (watch == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(watch);
//            }
//            if (user_id == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(user_id);
//            }
//            dest.writeString(status);
//            if (provider_id == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(provider_id);
//            }
//            dest.writeString(created_at);
//            dest.writeString(updated_at);
//            dest.writeString(provider_name);
//            dest.writeString(user_name);
//            dest.writeString(service_name_ar);
//            dest.writeString(service_name_en);
//            if (rat_count == null) {
//                dest.writeByte((byte) 0);
//            } else {
//                dest.writeByte((byte) 1);
//                dest.writeInt(rat_count);
//            }
//        }
//    }
}
