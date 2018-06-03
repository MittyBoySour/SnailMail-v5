package com.mad.snailmail_v5.Roaming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_INTENT_BUNDLE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_INTENT_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_TRANSITION_KEY;

public class RoamingEventIntentService extends IntentService {

    private static final String TAG = "RoamingEventIS";
    private RoamingActivity.IntentResultHandler mResultHandler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RoamingEventIntentService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        if (intent.hasExtra(PENDING_GEOFENCE_TRANSITION_KEY)) {
            mResultHandler = (RoamingActivity.IntentResultHandler) intent
                    .getParcelableExtra(PENDING_GEOFENCE_TRANSITION_KEY);
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @SuppressLint("RestrictedApi")
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, String.valueOf(geofencingEvent.getErrorCode()));
            return;
        }

        if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            ArrayList<String> geofenceKeyList = new ArrayList<>();
            for (Geofence geofence : triggeringGeofences) {
                geofenceKeyList.add(geofence.getRequestId());
            }
            Bundle geofenceKeyBundle = new Bundle();
            geofenceKeyBundle.putStringArrayList(PENDING_GEOFENCE_INTENT_KEY, geofenceKeyList);
            mResultHandler.send(PENDING_GEOFENCE_INTENT_BUNDLE, geofenceKeyBundle);

        } else {
            Log.e(TAG, String.valueOf(geofencingEvent.getGeofenceTransition()));
        }

    }

}
