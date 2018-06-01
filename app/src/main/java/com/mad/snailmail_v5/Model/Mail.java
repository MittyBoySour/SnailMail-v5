package com.mad.snailmail_v5.Model;

public class Mail {

    private String mSender;
    private String mRecipient;
    private String mFormattedSentTime;
    private String mGeofenceReference; // may also need delivery address
    private String mTitle;
    private String mMessage;
    private boolean mCollected;

    public Mail() {
        // Generic constructor
        mCollected = false;
    }

    private Mail(final Builder builder) {
        mSender = builder.mSender;
        mRecipient = builder.mRecipient;
        mTitle = builder.mTitle;
        mMessage = builder.mTitle;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        this.mSender = sender;
    }

    public String getRecipient() {
        return mRecipient;
    }

    public void setRecipient(String recipient) {
        this.mRecipient = recipient;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public boolean isCollected() {
        return mCollected;
    }

    public void setCollected(boolean collected) {
        this.mCollected = collected;
    }


    public static class Builder {

        private String mSender;
        private String mRecipient;
        private String mFormattedSentTime;
        private String mGeofenceReference; // may also need delivery address
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
