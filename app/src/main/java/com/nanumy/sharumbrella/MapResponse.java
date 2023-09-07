package com.nanumy.sharumbrella;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

public class MapResponse {
    @SerializedName("location")
    private Location location;

    @SerializedName("address")
    private String address;

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public static class Location {
        @SerializedName("latitude")
        private double latitude;

        @SerializedName("longitude")
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
