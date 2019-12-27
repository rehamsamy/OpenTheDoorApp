package com.m.openthedoorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceProviderModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("providerservice")
    private List<ProviderItem> providerservice;

    // Getters
    public Boolean getStatus() {
        return status;
    }

    public List<ProviderItem> getProviderservice() {
        return providerservice;
    }


    // class ProviderItem
    public static class ProviderItem implements Parcelable {
        @SerializedName("provider_id")
        private Integer provider_id;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("service_price")
        private Integer service_price;
        @SerializedName("rat_count")
        private Integer rat_count;
        @SerializedName("rat_sum")
        private String rat_sum;
        @SerializedName("long")
        private String lng;
        @SerializedName("lat")
        private String lat;
        @SerializedName("distance")
        private String distance;

        protected ProviderItem(Parcel in) {
            if (in.readByte() == 0) {
                provider_id = null;
            } else {
                provider_id = in.readInt();
            }
            name = in.readString();
            phone = in.readString();
            if (in.readByte() == 0) {
                service_price = null;
            } else {
                service_price = in.readInt();
            }
            if (in.readByte() == 0) {
                rat_count = null;
            } else {
                rat_count = in.readInt();
            }
            rat_sum = in.readString();
            lng = in.readString();
            lat = in.readString();
            distance = in.readString();
        }

        public static final Creator<ProviderItem> CREATOR = new Creator<ProviderItem>() {
            @Override
            public ProviderItem createFromParcel(Parcel in) {
                return new ProviderItem(in);
            }

            @Override
            public ProviderItem[] newArray(int size) {
                return new ProviderItem[size];
            }
        };

        // Getters
        public Integer getProvider_id() {
            return provider_id;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public Integer getService_price() {
            return service_price;
        }

        public Integer getRat_count() {
            return rat_count;
        }

        public String getRat_sum() {
            return rat_sum;
        }

        public String getLng() {
            return lng;
        }

        public String getLat() {
            return lat;
        }

        public String getDistance() {
            return distance;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (provider_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(provider_id);
            }
            dest.writeString(name);
            dest.writeString(phone);
            if (service_price == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(service_price);
            }
            if (rat_count == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(rat_count);
            }
            dest.writeString(rat_sum);
            dest.writeString(lng);
            dest.writeString(lat);
            dest.writeString(distance);
        }
    }
}
