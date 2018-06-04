/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mad.snailmail_v5.Roaming;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mad.snailmail_v5.MailList.MailListActivity;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_COMPOSITION_PERMISSIONS_REQUEST;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_INTENT_BUNDLE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.SYDNEY_LATITUDE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.SYDNEY_LONGITUDE;

public class RoamingActivity extends AppCompatActivity implements OnMapReadyCallback,
        RoamingContract.View {

    private FusedLocationProviderClient mFusedLocationClient;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private GoogleMap mMap;
    private Marker mMarker;
    private ArrayList<Marker> mMarkerList;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION_PERMISSIONS = 2001;

    private User mCurrentUser;
    private RoamingPresenter mPresenter;
    private TextView mLatitudeTV;
    private TextView mLongitudeTV;
    private Button mRoamingButton;
    private Button mStopRoamingButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roaming);

        mCurrentUser = new User();
        mCurrentUser.setUsername("TestUser0");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.roaming_map);
        mapFragment.getMapAsync(RoamingActivity.this);

        mLatitudeTV = (TextView) findViewById(R.id.latitude);
        mLongitudeTV = (TextView) findViewById(R.id.longitude);

        mStopRoamingButton = (Button) findViewById(R.id.stop_roaming);
        mStopRoamingButton.setOnClickListener(getStopRoamingClickListener());

        mMarkerList = new ArrayList<>();
        mPresenter = new RoamingPresenter(this, RoamingActivity.this);

    }

    /**
     * Provides a click listener to the roaming stop button to return to the mail list.
     */
    private View.OnClickListener getStopRoamingClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoamingActivity.this, MailListActivity.class));
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.geofencesRequested();
    }

    /**
     * sets map in focus and places a Circle in Sydney.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(getMapClickListener());
    }

    private GoogleMap.OnMapClickListener getMapClickListener() {
        return new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPresenter.mapClicked(latLng);
            }
        };
    }

    @Override
    public void setPresenter(RoamingContract.Presenter presenter) {
        mPresenter = (RoamingPresenter) presenter;
    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {
        dialogue.create();
    }

    /**
     * Generic method for displaying toasts passed by the presenter
     *
     * @param toast
     */
    @Override
    public void displayToast(Toast toast) {
        toast.show();
    }

    /**
     * Generic method for setting view to data loading state
     *
     * @param dataLoading
     */
    @Override
    public void setDataLoading(boolean dataLoading) {

    }

    /**
     * Requests permissions from the user passed by the presenter
     *
     * @param permissions
     */
    @Override
    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions,
                MAIL_COMPOSITION_PERMISSIONS_REQUEST);
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MAIL_COMPOSITION_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.permissionsNotSet(false, permissions);
                    Log.i(TAG, "onRequestPermissionsResult: permission granted");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: permission still not granted");
                }
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * Provides a new IntentResultReceiver to the Presenter
     */
    @Override
    public IntentResultHandler returnResultHandler() {
        return new IntentResultHandler(new Handler());
    }

    /**
     * Receives coordinates from the presenter to store in text views to display user's
     * location
     *
     * @param latitude
     * @param longitude
     */
    @Override
    public void setTextViewCoordinates(double latitude, double longitude) {
        mLatitudeTV.setText(String.valueOf(latitude));
        mLongitudeTV.setText(String.valueOf(longitude));
    }

    /**
     * List of CircleOptions objects to set up geofence representations
     *
     * @param circleOptionsList
     */
    @Override
    public void setGeofenceCirclesOnMap(ArrayList<CircleOptions> circleOptionsList) {
        for (CircleOptions circleOptions : circleOptionsList) {
            mMap.addCircle(circleOptions);
        }
    }

    /**
     * Provides a means of communication back to the activity from intent service
     * by extending the ResultReceiver class
     */
    public class IntentResultHandler extends ResultReceiver {

        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        @SuppressLint("RestrictedApi")
        public IntentResultHandler(Handler handler) {
            super(handler);
        }

        @SuppressLint("RestrictedApi")
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            // calls the presenter when a pending geofence event fires
            switch (resultCode) {
                case PENDING_GEOFENCE_INTENT_BUNDLE:
                    mPresenter.IntentResultBundleReceived(resultData);
                    break;
            }
        }

    }

}
