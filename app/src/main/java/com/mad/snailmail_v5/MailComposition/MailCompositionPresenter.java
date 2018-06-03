package com.mad.snailmail_v5.MailComposition;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mad.snailmail_v5.DeliveryLocator.DeliveryLocatorActivity;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;
import com.mad.snailmail_v5.Utilities.DeliveryManager;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATOR_ACTIVITY_REQUEST_CODE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.SYDNEY_LATITUDE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.SYDNEY_LONGITUDE;

class MailCompositionPresenter implements MailCompositionContract.Presenter {

    private FusedLocationProviderClient mFusedLocationClient;
    private final MailCompositionContract.View mMailCompositionView;
    private FirebaseManager mFirebaseManager;
    private DeliveryManager mDeliveryManager;
    private User mCurrentUser;
    private final Activity mActivityContext;
    private LatLng mDeliveryLocation;

    MailCompositionPresenter(MailCompositionContract.View mailCompositionView, Activity context) {
        // check for null
        this.mMailCompositionView = mailCompositionView;
        mMailCompositionView.setPresenter(this);
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
        mActivityContext = context;
        mDeliveryManager = DeliveryManager.getInstance();
    }

    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager.setUser(mCurrentUser);
    }

    @Override
    public void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList) {

    }

    @Override
    public void userListResponse() {

    }

    @Override
    public void submitMailButtonClicked(String title, String recipient, String message) {

        if (title.length() == 0 || recipient.length() == 0
                || message.length() == 0 || mDeliveryLocation == null) {

            mMailCompositionView.displayToast(Toast.makeText(
                    mActivityContext, R.string.fields_not_filled_toast, Toast.LENGTH_SHORT
            ));
            return;
        }

        // creating mail
        double[] coordinates = new double[4];
        LatLng sourceLocation = getUserCurrentLocation();
        LatLng deliveryLocation = mDeliveryLocation;
        coordinates[0] = sourceLocation.latitude;
        coordinates[1] = sourceLocation.longitude;
        coordinates[2] = deliveryLocation.latitude;
        coordinates[3] = deliveryLocation.longitude;


        Mail mail = new Mail.Builder()
                .setSender(mCurrentUser.getUsername())
                .setRecipient(recipient)
                .setSentTime(mDeliveryManager.getCurrentTime())
                .setArrivalTime(mDeliveryManager.getArrivalTime(sourceLocation, deliveryLocation))
                // set geofenceReference from DB
                .setSourceLatitude(sourceLocation.latitude)
                .setSourceLongitude(sourceLocation.longitude)
                .setDestinationLatitude(deliveryLocation.latitude)
                .setDestinationLongitude(deliveryLocation.longitude)
                .setTitle(title)
                .setMessage(message)
                .build();

        mFirebaseManager.sendMailToContact(mail);

        // async response should return geofence key into a new method
    }

    // should be in delivery manager

    private LatLng getUserCurrentLocation() {
        // request location permission
        // dummy loc
        return new LatLng(SYDNEY_LATITUDE, SYDNEY_LONGITUDE);
    }

    // should be in delivery manager

    @Override
    public void discardMailButtonClicked(String[] textFields) {
        // maybe show confirmation dialogue first
        // start mail list activity

        mMailCompositionView.displayDialogue(getDiscardMailDialogue());
    }

    private AlertDialog getDiscardMailDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivityContext);

        builder.setMessage(R.string.discard_mail_dialogue)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // start mail list activity
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss dialogue
                    }
                });

        return builder.create();
    }

    @Override
    public void deliveryLocationLongClicked() {

        Intent locatorIntent = new Intent(mActivityContext, DeliveryLocatorActivity.class);
        mActivityContext.startActivityForResult(locatorIntent, LOCATOR_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void setLocationClient(FusedLocationProviderClient fusedLocationProviderClient) {
        mFusedLocationClient = fusedLocationProviderClient;
    }

    public void requestLocation() {
        if (ContextCompat.checkSelfPermission(mActivityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMailCompositionView.requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION });
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getLocationSuccessListener());
    }

    @Override
    public void deliveryLocationClicked() {
        mMailCompositionView.displayToast(
                Toast.makeText(mActivityContext, R.string.location_selector_toast, Toast.LENGTH_SHORT));
    }

    private OnSuccessListener<Location> getLocationSuccessListener() {
        return new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    // set user last location in another class
                } else {
                    // dialogue to let user know they may have location services off or no reception
                }
            }
        };
    }


    //         mFusedLocationClient.getLastLocation().addOnSuccessListener(getLocationSuccessListener());

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
    public void permissionsNotSet(boolean firstRequest, String[] permissions) {
        if (!firstRequest) {
            mMailCompositionView.displayToast(getPermissionReasoningToast());
        }
        mMailCompositionView.requestPermissions(permissions);
    }

    public void setLocation(double latitude, double longitude, String markerTitle) {
        mMailCompositionView.updateDeliveryLocation(markerTitle);
        mDeliveryLocation = new LatLng(latitude, longitude);
    }

    public void setLocationCancelled() {
        // consider reusing dialogue
        mMailCompositionView.displayToast(getDeliveryLocationNeededToast());
    }

    private Toast getPermissionReasoningToast() {
        return Toast.makeText(mActivityContext, R.string.permission_reasoning_toast, Toast.LENGTH_LONG);
    }

    private Toast getDeliveryLocationNeededToast() {
        return Toast.makeText(mActivityContext, R.string.delivery_location_needed, Toast.LENGTH_LONG);
    }

}
