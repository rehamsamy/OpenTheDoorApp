package com.m.openthedoorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ServiceItemData implements Parcelable{
    @SerializedName("id")
    private Integer id;
    @SerializedName("provider_price_per_hour")
    private Integer provider_price_per_hour;
    @SerializedName("provider_minutes_to_arrive")
    private Integer provider_minutes_to_arrive;
    @SerializedName("cost_of_service_provider")
    private Integer cost_of_service_provider;
    @SerializedName("provider_hour_to_finish")
    private Integer provider_hour_to_finish;
    @SerializedName("bounce")
    private String bounce;
    @SerializedName("coupon_id")
    private Integer coupon_id;
    @SerializedName("coupon_value")
    private String coupon_value;
    @SerializedName("provider_long")
    private String provider_long;
    @SerializedName("provider_lat")
    private String provider_lat;
    @SerializedName("user_long")
    private String user_long;
    @SerializedName("user_lat")
    private String user_lat;
    @SerializedName("service_id")
    private Integer service_id;
    @SerializedName("total")
    private Integer total;
    @SerializedName("watch")
    private Integer watch;
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("status")
    private String status;
    @SerializedName("payment_method")
    private String payment_method;
    @SerializedName("provider_id")
    private Integer provider_id;
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
    @SerializedName("rat_count")
    private float rat_count;
    @SerializedName("rat_sum")
    private float rat_sum;
    @SerializedName("provider_phone")
    private String provider_phone;
    @SerializedName("provider_email")
    private String provider_email;
    @SerializedName("provider_image")
    private String provider_image;

    protected ServiceItemData(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            provider_price_per_hour = null;
        } else {
            provider_price_per_hour = in.readInt();
        }
        if (in.readByte() == 0) {
            provider_minutes_to_arrive = null;
        } else {
            provider_minutes_to_arrive = in.readInt();
        }
        if (in.readByte() == 0) {
            cost_of_service_provider = null;
        } else {
            cost_of_service_provider = in.readInt();
        }
        if (in.readByte() == 0) {
            provider_hour_to_finish = null;
        } else {
            provider_hour_to_finish = in.readInt();
        }
        bounce = in.readString();
        if (in.readByte() == 0) {
            coupon_id = null;
        } else {
            coupon_id = in.readInt();
        }
        coupon_value = in.readString();
        provider_long = in.readString();
        provider_lat = in.readString();
        user_long = in.readString();
        user_lat = in.readString();
        if (in.readByte() == 0) {
            service_id = null;
        } else {
            service_id = in.readInt();
        }
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
        if (in.readByte() == 0) {
            watch = null;
        } else {
            watch = in.readInt();
        }
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readInt();
        }
        status = in.readString();
        payment_method = in.readString();
        if (in.readByte() == 0) {
            provider_id = null;
        } else {
            provider_id = in.readInt();
        }
        created_at = in.readString();
        provider_name = in.readString();
        user_name = in.readString();
        service_name_ar = in.readString();
        service_name_en = in.readString();
        rat_count = in.readFloat();
        rat_sum = in.readFloat();
        provider_phone = in.readString();
        provider_email = in.readString();
        provider_image = in.readString();
    }

    public static final Creator<ServiceItemData> CREATOR = new Creator<ServiceItemData>() {
        @Override
        public ServiceItemData createFromParcel(Parcel in) {
            return new ServiceItemData(in);
        }

        @Override
        public ServiceItemData[] newArray(int size) {
            return new ServiceItemData[size];
        }
    };

    //Getter
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

    public Integer getCost_of_service_provider() {
        return cost_of_service_provider;
    }

    public Integer getCoupon_id() {
        return coupon_id;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public String getProvider_long() {
        return provider_long;
    }

    public String getProvider_lat() {
        return provider_lat;
    }

    public Integer getService_id() {
        return service_id;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getWatch() {
        return watch;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getProvider_id() {
        return provider_id;
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

    public String getProvider_phone() {
        return provider_phone;
    }

    public String getProvider_email() {
        return provider_email;
    }

    public String getProvider_image() {
        return provider_image;
    }

    public String getBounce() {
        return bounce;
    }

    public String getUser_long() {
        return user_long;
    }

    public String getUser_lat() {
        return user_lat;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public float getRat_count() {
        return rat_count;
    }

    public float getRat_sum() {
        return rat_sum;
    }

    public static Creator<ServiceItemData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (provider_price_per_hour == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provider_price_per_hour);
        }
        if (provider_minutes_to_arrive == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provider_minutes_to_arrive);
        }
        if (cost_of_service_provider == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cost_of_service_provider);
        }
        if (provider_hour_to_finish == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provider_hour_to_finish);
        }
        dest.writeString(bounce);
        if (coupon_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(coupon_id);
        }
        dest.writeString(coupon_value);
        dest.writeString(provider_long);
        dest.writeString(provider_lat);
        dest.writeString(user_long);
        dest.writeString(user_lat);
        if (service_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(service_id);
        }
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(total);
        }
        if (watch == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(watch);
        }
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(user_id);
        }
        dest.writeString(status);
        dest.writeString(payment_method);
        if (provider_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(provider_id);
        }
        dest.writeString(created_at);
        dest.writeString(provider_name);
        dest.writeString(user_name);
        dest.writeString(service_name_ar);
        dest.writeString(service_name_en);
        dest.writeFloat(rat_count);
        dest.writeFloat(rat_sum);
        dest.writeString(provider_phone);
        dest.writeString(provider_email);
        dest.writeString(provider_image);
    }
}
