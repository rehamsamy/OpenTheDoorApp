package com.m.openthedoorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponseModel {
    @SerializedName("status")
    private Boolean status;
    @SerializedName("reviews")
    private List<ReviewData> reviews;

    //Getter
    public Boolean getStatus() {
        return status;
    }

    public List<ReviewData> getReviews() {
        return reviews;
    }

    // Class REview Data ....
    public static class ReviewData implements Parcelable {
        @SerializedName("id")
        private Integer id;
        @SerializedName("active")
        private String active;
        @SerializedName("watch")
        private String watch;
        @SerializedName("notes")
        private String notes;
        @SerializedName("rate")
        private Integer rate;
        @SerializedName("provider_id")
        private Integer provider_id;
        @SerializedName("user_id")
        private Integer user_id;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("user_name")
        private String user_name;
        @SerializedName("user_image")
        private String user_image;
        @SerializedName("provider_name")
        private String provider_name;
        @SerializedName("provider_image")
        private String provider_image;

        protected ReviewData(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            active = in.readString();
            watch = in.readString();
            notes = in.readString();
            if (in.readByte() == 0) {
                rate = null;
            } else {
                rate = in.readInt();
            }
            if (in.readByte() == 0) {
                provider_id = null;
            } else {
                provider_id = in.readInt();
            }
            if (in.readByte() == 0) {
                user_id = null;
            } else {
                user_id = in.readInt();
            }
            created_at = in.readString();
            user_name = in.readString();
            user_image = in.readString();
            provider_name = in.readString();
            provider_image = in.readString();
        }

        public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
            @Override
            public ReviewData createFromParcel(Parcel in) {
                return new ReviewData(in);
            }

            @Override
            public ReviewData[] newArray(int size) {
                return new ReviewData[size];
            }
        };

        // Getters
        public Integer getId() {
            return id;
        }

        public String getActive() {
            return active;
        }

        public String getWatch() {
            return watch;
        }

        public String getNotes() {
            return notes;
        }

        public Integer getRate() {
            return rate;
        }

        public Integer getProvider_id() {
            return provider_id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getProvider_name() {
            return provider_name;
        }

        public String getProvider_image() {
            return provider_image;
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
            dest.writeString(active);
            dest.writeString(watch);
            dest.writeString(notes);
            if (rate == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(rate);
            }
            if (provider_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(provider_id);
            }
            if (user_id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(user_id);
            }
            dest.writeString(created_at);
            dest.writeString(user_name);
            dest.writeString(user_image);
            dest.writeString(provider_name);
            dest.writeString(provider_image);
        }
    }
}
