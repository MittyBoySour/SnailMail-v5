package com.mad.snailmail_v5.Roaming;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;
import com.mad.snailmail_v5.Utilities.DeliveryManager;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.GEOFENCE_PENDING_INTENT_REQUEST;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.GEOFENCE_RADIUS_IN_METRES;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_INTENT_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_TRANSITION_KEY;

public class RoamingPresenter implements RoamingContract.Presenter {

    private final String MOCK_PROVIDER = "mock_provider";

    private FusedLocationProviderClient mFusedLocationClient;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private final RoamingContract.View mView;
    private final Activity mActivityContext;
    private final DeliveryManager mDeliveryManager;
    private User mCurrentUser;
    private FirebaseManager mFirebaseManager;
    private PendingIntent mGeofencePendingIntent;
    private GeofencingRequest mGeofencingRequest;
    private RoamingActivity.IntentResultHandler mResultHandler;
    private ArrayList<CircleOptions> mCircleOptionsList;

    public RoamingPresenter(RoamingContract.View view, Context context) {
        mView = view;
        mActivityContext = (Activity) context;
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
        mDeliveryManager = DeliveryManager.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivityContext);
        mGeofencingClient = LocationServices.getGeofencingClient(mActivityContext);
    }


    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager.setUser(mCurrentUser);
    }


    @Override
    public void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList) {
        mGeofenceList = convertToGeofenceList(bareGeofenceList);
        mGeofencingRequest = getGeofencingRequest();
        mCircleOptionsList = getCircleOptionsList(bareGeofenceList);

        if (ContextCompat.checkSelfPermission(mActivityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mView.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION });
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(mActivityContext, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        mView.displayToast(Toast.makeText(
                                mActivityContext, R.string.geofences_succesful, Toast.LENGTH_SHORT
                        ));
                    }
                })
                .addOnFailureListener(mActivityContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        e.printStackTrace();
                    }
                });

        mView.setGeofenceCirclesOnMap(mCircleOptionsList);

    }

    private ArrayList<CircleOptions> getCircleOptionsList(ArrayList<BareGeofence> bareGeofenceList) {
        ArrayList<CircleOptions> circleOptionsList = new ArrayList<>();
        CircleOptions circleOptions;

        for (BareGeofence bareGeofence : bareGeofenceList) {
            circleOptions = new CircleOptions()
                    .center(new LatLng(bareGeofence.getLatitude(), bareGeofence.getLongitude()))
                    .radius(GEOFENCE_RADIUS_IN_METRES)
                    .strokeColor(Color.RED)
                    .fillColor(Color.BLUE);
            circleOptionsList.add(circleOptions);
        }
        return circleOptionsList;
    }

    @Override
    public void userListResponse() {

    }

    @Override
    public void userExistenceResponse(boolean userExists) {

    }

    @Override
    public void userSuccessfullyAdded() {

    }

    private ArrayList<Geofence> convertToGeofenceList(ArrayList<BareGeofence> bareGeofenceList) {
        ArrayList<Geofence> geofenceList = new ArrayList<>();

        for (BareGeofence bareGeofence: bareGeofenceList) {
            geofenceList.add(new Geofence.Builder()
                    .setRequestId(bareGeofence.getGeofenceReferenceKey())
                    .setCircularRegion(
                            bareGeofence.getLatitude(),
                            bareGeofence.getLongitude(),
                            GEOFENCE_RADIUS_IN_METRES
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());

        }

        return geofenceList;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(mActivityContext, RoamingEventIntentService.class);
        mResultHandler = mView.returnResultHandler();

        intent.putExtra(PENDING_GEOFENCE_TRANSITION_KEY, mResultHandler);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(
                mActivityContext, GEOFENCE_PENDING_INTENT_REQUEST,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    @Override
    public void permissionsNotSet(boolean firstRequest, String[] permissions) {
        if (!firstRequest) {
            mView.displayToast(getPermissionReasoningToast());
        }
        mView.requestPermissions(permissions);
    }

    private Toast getPermissionReasoningToast() {
        return Toast.makeText(mActivityContext, R.string.permission_reasoning_toast, Toast.LENGTH_LONG);
    }


    @Override
    public void start() {

    }

    @Override
    public void userMailListResponse(ArrayList<Mail> mailList) {

    }

    @Override
    public void updateUserContactList(ArrayList<String> UserContactList) {

    }

    @Override
    public void geofencesRequested() {
        mFirebaseManager.updateGeofences();
    }

    @Override
    public void IntentResultBundleReceived(Bundle resultData) {
        if (resultData.containsKey(PENDING_GEOFENCE_INTENT_KEY)) {
            ArrayList<String> triggeredGeoRefKeys = resultData
                    .getStringArrayList(PENDING_GEOFENCE_INTENT_KEY);

            mFirebaseManager.updateMailGeofenceCollected(triggeredGeoRefKeys);
        }
    }

    @Override
    public void mapClicked(LatLng latLng) {
        mView.displayToast(Toast.makeText(
                mActivityContext, R.string.simulation_mode, Toast.LENGTH_LONG
        ));
        mView.setTextViewCoordinates(latLng.latitude, latLng.longitude);
        if (ContextCompat.checkSelfPermission(mActivityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mView.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION });
        }
        Location mockLocation = new Location(MOCK_PROVIDER);
        mockLocation.setLatitude(latLng.latitude);
        mockLocation.setLongitude(latLng.longitude);
        mFusedLocationClient.setMockLocation(mockLocation);
    }

}
