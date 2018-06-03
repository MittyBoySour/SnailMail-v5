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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(SYDNEY_LATITUDE, SYDNEY_LONGITUDE))
                .radius(10000)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));

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

    @Override
    public void displayToast(Toast toast) {
        toast.show();
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

    @Override
    public void setTextViewCoordinates(double latitude, double longitude) {
        mLatitudeTV.setText(String.valueOf(latitude));
        mLongitudeTV.setText(String.valueOf(longitude));
    }

    @Override
    public void setGeofenceCirclesOnMap(ArrayList<CircleOptions> circleOptionsList) {
        for (CircleOptions circleOptions : circleOptionsList) {
            mMap.addCircle(circleOptions);
        }
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
