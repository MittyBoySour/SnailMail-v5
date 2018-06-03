package com.mad.snailmail_v5.Model;

import java.util.HashMap;

public class BareGeofence {

    public final String IS_ACTIVE_HASH_KEY = "isActive";
    public final String LATITUDE_HASH_KEY = "latitude";
    public final String LONGITUDE_HASH_KEY = "longitude";

    private String mGeofenceReferenceKey;
    private boolean mIsActive;
    private double mLatitude;
    private double mLongitude;

    public BareGeofence() {

    }

    public BareGeofence(boolean isActive, double latitude, double longitude) {
        this.mIsActive = isActive;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean isActive) {
        this.mIsActive = isActive;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getGeofenceReferenceKey() {
        return mGeofenceReferenceKey;
    }

    public void setGeofenceReferenceKey(String geofenceReferenceKey) {
        this.mGeofenceReferenceKey = geofenceReferenceKey;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> bareGeofenceMap = new HashMap<>();
        bareGeofenceMap.put(IS_ACTIVE_HASH_KEY, mIsActive);
        bareGeofenceMap.put(LATITUDE_HASH_KEY, mLatitude);
        bareGeofenceMap.put(LONGITUDE_HASH_KEY, mLongitude);

        return bareGeofenceMap;
    }

}