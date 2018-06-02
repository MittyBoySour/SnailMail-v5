package com.mad.snailmail_v5.MailComposition;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mad.snailmail_v5.R;

public class MailCompositionFragment extends Fragment implements MailCompositionContract.View {

    private static final String TAG = "MailCompositionFragment";
    private static final int REQUEST_LOCATION_PERMISSIONS = 2001;

    private MailCompositionContract.Presenter mPresenter;

    private Button mSubmitMailButton;
    private Button mDiscardMailButton;
    private EditText mTitleTV;
    private EditText mRecipientTV;
    private TextView mDeliveryLocationTV; // store in other activity, and don't allow click until selected
    private EditText mMessageTV;
    private MapView mMapView;
    private GoogleMap mMap;
    private Marker mMarker;
    private FusedLocationProviderClient mFusedLocationClient;
    private GeofencingClient mGeofencingClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mGeofencingClient = LocationServices.getGeofencingClient(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign to the xml file

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) { /* request permissions */
            Log.i(TAG, "onCreate: permission was not granted");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);

        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(getLocationSuccessListener());
    }

    private OnSuccessListener<Location> getLocationSuccessListener() {
        return new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    Log.i(TAG, "onSuccess: location is not null");
                    // mLatitudeTV.setText(location.getLatitude() + "");
                    // mLongitudeTV.setText(location.getLongitude() + "");
                } else {
                    Log.i(TAG, "onSuccess: location is null");
                    // mLatitudeTV.setText("null");
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // could potentially move this all to onCreate if no other frag/state will exist for activity

        View root = inflater.inflate(R.layout.mail_composition_frag, container, false);

        mSubmitMailButton = (Button) root.findViewById(R.id.send_composition);
        mSubmitMailButton.setOnClickListener(getSubmitMailButtonListener());
        mDiscardMailButton = (Button) root.findViewById(R.id.discard_composition);
        mDiscardMailButton.setOnClickListener(getDiscardMailButtonListener());

        mTitleTV = (EditText) root.findViewById(R.id.composition_title);
        mRecipientTV = (EditText) root.findViewById(R.id.composition_recipient);

        mDeliveryLocationTV = (TextView) root.findViewById(R.id.delivery_address);
        mDeliveryLocationTV.setOnLongClickListener(getDeliveryLocationFieldLongClickListener());
        checkDeliveryLocation();

        mMessageTV = (EditText) root.findViewById(R.id.composition_message);



        // Map initialisation
        mMapView = (MapView) root.findViewById(R.id.composition_map);
        mMapView.setVisibility(View.GONE);
        mMapView.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // mMapView.getMapAsync(getMapReadyCallback()); probably call on longClick()

        return root;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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

    private OnMapReadyCallback getMapReadyCallback() {
        return new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // For showing a move to my location button
                // mMap.setMyLocationEnabled(true); // add permission ask earlier than this

                mMap = googleMap;
                mMap.setOnMapLongClickListener(getLongClickListener());
                // may need to add marker click listener
                mMap.setOnMarkerClickListener(getMarkerClickListener());
                // marker initially on sydney
                LatLng sydney = new LatLng(-33.852, 151.211);
                MarkerOptions options = new MarkerOptions().position(sydney)
                        .title("Marker in Sydney");

                Log.i(TAG, "onMapReady: sydney latitude :" + sydney.latitude + ", sydney.longitude: " + sydney.longitude);
                mMarker = mMap.addMarker(options);
                Log.i(TAG, "onMapReady: mMarker latitude :" + mMarker.getPosition().latitude + ", mMarker longitude: " + mMarker.getPosition().longitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            }
        };
    }

    private GoogleMap.OnMapLongClickListener getLongClickListener() {
        return new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // remove any existing marker first
                // maybe add favourite places
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("User marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Log.i(TAG, "onMapLongClick: mMarker latitude :" + mMarker.getPosition().latitude + ", mMarker longitude: " + mMarker.getPosition().longitude);

            }
        };
    }

    private GoogleMap.OnMarkerClickListener getMarkerClickListener() {
        return new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "onMarkerClick: " + marker.getPosition().latitude);
                // call presenter

                // mLatitudeTV.setText(marker.getPosition().latitude + "");
                // mLongitudeTV.setText(marker.getPosition().longitude + "");
                // check marker and add either address or coordinates to geofence

                return false;
            }
        };
    }

    @Override
    public void setPresenter(MailCompositionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static MailCompositionFragment newInstance() {
        return new MailCompositionFragment();
    }

    public View.OnClickListener getSubmitMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleTV.getText().toString();
                String recipient = mRecipientTV.getText().toString();
                // LatLng deliveryLocation;
                String message = mMessageTV.getText().toString();
                mPresenter.submitMailButtonClicked(title, recipient, message);
            }
        };
    }

    private View.OnClickListener getDiscardMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.discardMailButtonClicked();
            }
        };
    }

    public View.OnLongClickListener getDeliveryLocationFieldLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // start locator activity
                // possibly make into fragment rather than activity and bundle data

                boolean displayingMap = false;
                if (mMapView.getVisibility() == View.VISIBLE) displayingMap = true;
                mPresenter.DeliveryLocationLongClicked(displayingMap);

                return false;
            }
        };
    }

    private void checkDeliveryLocation() {
        // check the passed data to see if location has been selected

    }


    @Override
    public void showMiddleView(int viewOption) {
        mMessageTV.setVisibility(View.GONE);
        mMapView.setVisibility(View.GONE);
        switch(viewOption) {
            case 0:
                mMessageTV.setVisibility(View.VISIBLE);
            case 1:
                mMapView.setVisibility(View.VISIBLE);
                break;
            default:
                // contactView
        }
    }

}
