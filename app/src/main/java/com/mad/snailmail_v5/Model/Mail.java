package com.mad.snailmail_v5.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mail implements Parcelable {

    private String mSender;
    private String mRecipient;
    private long mSentTime;
    private long mArrivalTime;
    private double mSourceLatitude;
    private double mSourceLongitude;
    private double mDestinationLatitude;
    private double mDestinationLongitude;
    private String mGeofenceReferenceKey;
    private String mTitle;
    private String mMessage;
    private boolean mDelivered;
    private boolean mCollected;

    public Mail() {
        // Generic constructor
        mDelivered = false;
        mCollected = false;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getRecipient() {
        return mRecipient;
    }

    public void setRecipient(String recipient) {
        mRecipient = recipient;
    }

    public long getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        mArrivalTime = arrivalTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public double getSourceLatitude() {
        return this.mSourceLatitude;
    }

    public void setSourceLatitude(double latitude) {
        mSourceLatitude = latitude;
    }

    public double getSourceLongitude() {
        return this.mSourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.mSourceLongitude = sourceLongitude;
    }

    public double getDestinationLatitude() {
        return this.mDestinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.mDestinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return this.mDestinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.mDestinationLongitude = destinationLongitude;
    }

    public String getGeofenceReferenceKey() {
        return mGeofenceReferenceKey;
    }

    public void setGeofenceReferenceKey(String geofenceReferenceKey) {
        mGeofenceReferenceKey = geofenceReferenceKey;
    }

    public boolean isDelivered() {
        return mDelivered;
    }

    public void setDelivered(boolean delivered) {
        this.mDelivered = delivered;
    }

    public boolean isCollected() {
        return mCollected;
    }

    public void setCollected(boolean collected) {
        mCollected = collected;
    }

    ////////////////// HASH MAP IMPLEMENTATION //////////////////////////



    ////////////////// PARCELABLE IMPLEMENTATION /////////////////////

    protected Mail(Parcel in) {
        mSender = in.readString();
        mRecipient = in.readString();
        mSentTime = in.readLong();
        mArrivalTime = in.readLong();
        mSourceLatitude = in.readDouble();
        mSourceLongitude = in.readDouble();
        mDestinationLatitude = in.readDouble();
        mDestinationLongitude = in.readDouble();
        mGeofenceReferenceKey = in.readString();
        mTitle = in.readString();
        mMessage = in.readString();
        mDelivered = in.readByte() != 0;
        mCollected = in.readByte() != 0;
    }

    public static final Creator<Mail> CREATOR = new Creator<Mail>() {
        @Override
        public Mail createFromParcel(Parcel in) {
            return new Mail(in);
        }

        @Override
        public Mail[] newArray(int size) {
            return new Mail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSender);
        dest.writeString(mRecipient);
        dest.writeLong(mSentTime);
        dest.writeLong(mArrivalTime);
        dest.writeDouble(mSourceLatitude);
        dest.writeDouble(mSourceLongitude);
        dest.writeDouble(mDestinationLatitude);
        dest.writeDouble(mDestinationLongitude);
        dest.writeString(mGeofenceReferenceKey);
        dest.writeString(mTitle);
        dest.writeString(mMessage);
        dest.writeByte((byte) (mDelivered ? 1 : 0));
        dest.writeByte((byte) (mCollected ? 1 : 0));
    }

    ///////////////// BUILDER IMPLEMENTATION ////////////////

    private Mail(final Builder builder) {
        mSender = builder.mSender;
        mRecipient = builder.mRecipient;
        mSentTime = builder.mSentTime;
        mArrivalTime = builder.mArrivalTime;
        mSourceLatitude = builder.mSourceLatitude;
        mSourceLongitude = builder.mSourceLongitude;
        mDestinationLatitude = builder.mDestinationLatitude;
        mDestinationLongitude = builder.mDestinationLongitude;
        mGeofenceReferenceKey = builder.mGeofenceReference;
        mTitle = builder.mTitle;
        mMessage = builder.mTitle;
    }

    public static class Builder {

        private String mSender;
        private String mRecipient;
        private long mSentTime;
        private long mArrivalTime;
        private double mSourceLatitude;
        private double mSourceLongitude;
        private double mDestinationLatitude;
        private double mDestinationLongitude;
        private String mGeofenceReference;
        private String mTitle;
        private String mMessage;

        public Builder setSender(String sender) {
            this.mSender = sender;
            return this;
        }

        public Builder setRecipient(String recipient) {
            this.mRecipient = recipient;
            return this;
        }

        public Builder setSentTime(long sentTime) {
            this.mSentTime = sentTime;
            return this;
        }

        public Builder setArrivalTime(long arrivalTime) {
            this.mArrivalTime = arrivalTime;
            return this;
        }

        public Builder setSourceLatitude(double sourceLatitude) {
            this.mSourceLatitude = sourceLatitude;
            return this;
        }

        public Builder setSourceLongitude(double sourceLongitude) {
            this.mSourceLongitude = sourceLongitude;
            return this;
        }

        public Builder setDestinationLatitude(double destinationLatitude) {
            this.mDestinationLatitude = destinationLatitude;
            return this;
        }

        public Builder setDestinationLongitude(double destinationLongitude) {
            this.mDestinationLongitude = destinationLongitude;
            return this;
        }

        public Builder setGeofenceReference(String geofenceReference) {
            this.mGeofenceReference = geofenceReference;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Mail build() {
            return new Mail(this);
        }

    }
}
