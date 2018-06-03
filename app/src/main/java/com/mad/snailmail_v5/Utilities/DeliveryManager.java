package com.mad.snailmail_v5.Utilities;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.mad.snailmail_v5.Model.Mail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DeliveryManager {

    private static final String TAG = "DeliveryManager";

    public enum Speed {
        AVERAGE_CARRIER_PIGEON, AVERAGE_GARDEN_SNAIL
    }

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final double AVERAGE_CARRIER_PIGEON_DELIVERY_SPEED = 80.00;
    private static final double AVERAGE_GARDEN_SNAIL_DELIVERY_SPEED = 0.047;
    private static double mSelectedDeliverySpeed;

    private static DeliveryManager sInstance;

    private DeliveryManager() {
        setDeliverySpeed(Speed.AVERAGE_CARRIER_PIGEON);
    }

    public static DeliveryManager getInstance() {
        if (sInstance == null) {
            sInstance = new DeliveryManager();
        }
        return sInstance;
    }

    public boolean mailDelivered(Mail mail) {
        return getCurrentTime() > mail.getArrivalTime();
    }

    public String toDisplayTime(long arrivalTime) {
        Date dateTime = new Date(arrivalTime);
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        return formatter.format(dateTime);
    }

    public long getArrivalTime(LatLng sourceLocation, LatLng deliveryLocation) {
        return getCurrentTime() + getTransitDuration(sourceLocation, deliveryLocation);
    }

    public long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private long getTransitDuration(LatLng sourceLocation, LatLng deliveryLocation) {
        Location loc1 = new Location("");
        loc1.setLatitude(sourceLocation.latitude);
        loc1.setLongitude(sourceLocation.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(deliveryLocation.latitude);
        loc2.setLongitude(deliveryLocation.longitude);

        double distance = loc1.distanceTo(loc2);
        return (long)(distance / mSelectedDeliverySpeed);
    }

    public void setDeliverySpeed(Speed speed) {
        switch(speed) {
            case AVERAGE_CARRIER_PIGEON:
                mSelectedDeliverySpeed = AVERAGE_CARRIER_PIGEON_DELIVERY_SPEED;
                break;
            case AVERAGE_GARDEN_SNAIL:
                mSelectedDeliverySpeed = AVERAGE_GARDEN_SNAIL_DELIVERY_SPEED;
        }
    }
}
