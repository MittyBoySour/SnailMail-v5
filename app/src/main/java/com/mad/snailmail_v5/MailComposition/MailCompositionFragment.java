package com.mad.snailmail_v5.MailComposition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;
import com.mad.snailmail_v5.R;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_COMPOSITION_PERMISSIONS_REQUEST;

public class MailCompositionFragment extends Fragment implements MailCompositionContract.View {

    private static final String TAG = "MailCompositionFragment";

    private MailCompositionContract.Presenter mPresenter;

    private Button mSubmitMailButton;
    private Button mDiscardMailButton;
    private EditText mTitleET;
    private EditText mRecipientET;
    private TextView mDeliveryLocationTV; // store in other activity, and don't allow click until selected
    private EditText mMessageET;
    private FusedLocationProviderClient mFusedLocationClient;
    private GeofencingClient mGeofencingClient;
    private Activity mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (Activity) context;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mGeofencingClient = LocationServices.getGeofencingClient(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign to the xml file

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mPresenter.permissionsNotSet(true, new String[] { Manifest.permission.ACCESS_FINE_LOCATION });
        }
        mPresenter.setLocationClient(mFusedLocationClient);
        mPresenter.requestLocation();
    }

    @Override
    public void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(mContext, permissions,
                MAIL_COMPOSITION_PERMISSIONS_REQUEST);
    }

    @Override
    public void displayToast(Toast toast) {
        toast.show();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // could potentially move this all to onCreate if no other frag/state will exist for activity

        View root = inflater.inflate(R.layout.mail_composition_frag, container, false);

        mSubmitMailButton = (Button) root.findViewById(R.id.send_composition);
        mSubmitMailButton.setOnClickListener(getSubmitMailButtonListener());
        mDiscardMailButton = (Button) root.findViewById(R.id.discard_composition);
        mDiscardMailButton.setOnClickListener(getDiscardMailButtonListener());

        mTitleET = (EditText) root.findViewById(R.id.composition_title);
        mRecipientET = (EditText) root.findViewById(R.id.composition_recipient);

        mDeliveryLocationTV = (TextView) root.findViewById(R.id.delivery_address);
        mDeliveryLocationTV.setOnLongClickListener(getDeliveryLocationFieldLongClickedListener());
        mDeliveryLocationTV.setOnClickListener(getDeliveryLocationFieldClickedListener());
        checkDeliveryLocation();

        mMessageET = (EditText) root.findViewById(R.id.composition_message);

        return root;
    }

    @Override
    public void setPresenter(MailCompositionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {
        dialogue.show();
    }

    public static MailCompositionFragment newInstance() {
        return new MailCompositionFragment();
    }

    public View.OnClickListener getSubmitMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleET.getText().toString();
                String recipient = mRecipientET.getText().toString();
                // LatLng deliveryLocation;
                String message = mMessageET.getText().toString();
                mPresenter.submitMailButtonClicked(title, recipient, message);
            }
        };
    }

    private View.OnClickListener getDiscardMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] textFields = new String[4];
                textFields[0] = mTitleET.getText().toString();
                mPresenter.discardMailButtonClicked(textFields);
            }
        };
    }

    public View.OnLongClickListener getDeliveryLocationFieldLongClickedListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mPresenter.deliveryLocationLongClicked();

                return false;
            }
        };
    }

    public View.OnClickListener getDeliveryLocationFieldClickedListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPresenter.deliveryLocationClicked();

            }
        };
    }


    private void checkDeliveryLocation() {
        // check the passed data to see if location has been selected

    }

    @Override
    public void updateDeliveryLocation(String markerTitle) {
        mDeliveryLocationTV.setText(markerTitle);
    }

}
