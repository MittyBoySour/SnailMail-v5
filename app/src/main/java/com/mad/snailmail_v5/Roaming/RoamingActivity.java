package com.mad.snailmail_v5.Roaming;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_COMPOSITION_PERMISSIONS_REQUEST;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.PENDING_GEOFENCE_INTENT_BUNDLE;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_locator);

        mCurrentUser = new User();
        mCurrentUser.setUsername("TestUser0");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.delivery_locator_map);
        mapFragment.getMapAsync(RoamingActivity.this);

        mMarkerList = new ArrayList<>();
        mPresenter = new RoamingPresenter(this, RoamingActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.geofencesRequested();
    }

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

    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {

    }

    @Override
    public void displayToast(Toast toast) {

    }

    @Override
    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions,
                MAIL_COMPOSITION_PERMISSIONS_REQUEST);
    }

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

    @Override
    public IntentResultHandler returnResultHandler() {
        return new IntentResultHandler(new Handler());
    }

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

            switch (resultCode) {
                case PENDING_GEOFENCE_INTENT_BUNDLE:
                    mPresenter.IntentResultBundleReceived(resultData);
                    break;
            }
        }

    }

}
