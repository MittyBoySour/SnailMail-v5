package com.mad.snailmail_v5.DeliveryLocator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.MELBOURNE_LATITUDE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.LocationConstants.MELBOURNE_LONGITUDE;

public class DeliveryLocatorActivity extends AppCompatActivity implements OnMapReadyCallback,
        DeliveryLocatorContract.View {

    private FusedLocationProviderClient mFusedLocationClient;
    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private GoogleMap mMap;
    private Marker mMarker;
    private ArrayList<Marker> mMarkerList;

    private static final String TAG = "MainActivity";
    private static final int REQUEST_LOCATION_PERMISSIONS = 2001;

    private User mCurrentUser;
    private DeliveryLocatorPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_locator);

        mCurrentUser = new User();
        mCurrentUser.setUsername("TestUser0");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mGeofencingClient = LocationServices.getGeofencingClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.delivery_locator_map);
        mapFragment.getMapAsync(DeliveryLocatorActivity.this);

        mMarkerList = new ArrayList<>();
        mPresenter = new DeliveryLocatorPresenter(this, DeliveryLocatorActivity.this);
        /*
        mGeofenceList.add(new Geofence.Builder().setRequestId("first")
                .setCircularRegion()
                .build());
        */
    }

    private GoogleMap.OnMapLongClickListener getLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // remove any existing marker first
                // maybe add favourite places

                mPresenter.mapLongClicked(latLng);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult: permission granted");
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: permission still not granted");
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.activityFinished();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMapLongClickListener(getLongClickListener());
        // may need to add marker click listener
        mMap.setOnMarkerClickListener(getMarkerClickListener());
        // marker initially on sydney
        LatLng sydney = new LatLng(MELBOURNE_LATITUDE, MELBOURNE_LONGITUDE);
        MarkerOptions options = new MarkerOptions().position(sydney)
                .title("Marker in Sydney");

        // get current location and set if not null

        // Log.i(TAG, "onMapReady: sydney latitude :" + sydney.latitude + ", sydney.longitude: " + sydney.longitude);
        Marker marker = mMap.addMarker(options);
        mMarkerList.add(marker);
        // Log.i(TAG, "onMapReady: mMarker latitude :" + mMarker.getPosition().latitude + ", mMarker longitude: " + mMarker.getPosition().longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    private GoogleMap.OnMarkerClickListener getMarkerClickListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // check marker and add either address or coordinates to geofence
                mPresenter.markerClicked(marker);

                // Log.i(TAG, "onMarkerClick: " + marker.getPosition().latitude);
                return false;
            }
        };
    }

    @Override
    public void addMarkerFromOptions(MarkerOptions options) {
        Marker marker = mMap.addMarker(options);
        mMarkerList.add(marker);
    }

    @Override
    public void moveCamera(CameraUpdate cameraUpdate) {
        mMap.moveCamera(cameraUpdate);
    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {
        dialogue.show();
    }

    @Override
    public void requestPermissions(String[] permissions) {

    }

    @Override
    public void displayToast(Toast toast) {

    }

    @Override
    public void setPresenter(DeliveryLocatorContract.Presenter presenter) {

    }
}
