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


    public boolean isCollected() {
        return mCollected;
    }

    public void setCollected(boolean collected) {
        this.mCollected = collected;
    }
}
