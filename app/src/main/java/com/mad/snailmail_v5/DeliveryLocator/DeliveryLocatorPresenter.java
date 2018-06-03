package com.mad.snailmail_v5.DeliveryLocator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_LATITUDE_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_LONGITUDE_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_TITLE_KEY;

public class DeliveryLocatorPresenter implements DeliveryLocatorContract.Presenter {

    private final DeliveryLocatorContract.View mView;
    private final Activity mActivityContext;
    private String mCurrentMarkerTitle;

    public DeliveryLocatorPresenter(DeliveryLocatorContract.View view, Context context) {
        mView = view;
        mActivityContext = (Activity) context;
        mCurrentMarkerTitle = "";
    }

    @Override
    public void setCurrentUser(User user) {

    }

    @Override
    public void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList) {

    }

    @Override
    public void userListResponse() {

    }

    public void mapLongClicked(LatLng latLng) {
        mView.displayDialogue(getMapLongClickedDialogue(latLng));
        // Log.i(TAG, "onMapLongClick: mMarker latitude :" + mMarker.getPosition().latitude + ", mMarker longitude: " + mMarker.getPosition().longitude);
    }

    @Override
    public void markerClicked(Marker marker) {
        // confirmation dialogue of setting as
        mView.displayDialogue(getMarkerClickedDialogue(marker));
    }

    @Override
    public void activityFinished() {
        mActivityContext.setResult(Activity.RESULT_CANCELED);
        mActivityContext.finish();
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
    public void permissionsNotSet(boolean firstRequest, String[] permissions) {

    }

    private AlertDialog getMapLongClickedDialogue(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivityContext);

        // Set up the input

//        // I'm using fragment here so I'm using getView() to provide ViewGroup
//// but you can provide here any other instance of ViewGroup from your Fragment / Activity
//        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_inpu_password, (ViewGroup) getView(), false);
//// Set up the input
//        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
//// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        builder.setView(viewInflated);

        final EditText input = new EditText(mActivityContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setMessage(R.string.name_marker_dialogue)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCurrentMarkerTitle = input.getText().toString();
                        mView.addMarkerFromOptions(new MarkerOptions().position(latLng).title(mCurrentMarkerTitle));
                        mView.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
        return builder.create();
    }

    private AlertDialog getMarkerClickedDialogue(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivityContext);
        builder.setMessage(R.string.select_marker_dialogue)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // return activity
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(LOCATION_LATITUDE_KEY, marker.getPosition().latitude);
                        returnIntent.putExtra(LOCATION_LONGITUDE_KEY, marker.getPosition().latitude);
                        returnIntent.putExtra(LOCATION_TITLE_KEY, marker.getTitle());
                        mActivityContext.setResult(Activity.RESULT_OK, returnIntent);
                        mActivityContext.finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        return builder.create();

    }

}
