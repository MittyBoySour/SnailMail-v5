package com.mad.snailmail_v5.MailComposition;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.mad.snailmail_v5.R;

import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.SignIn.SignInActivity;
import com.mad.snailmail_v5.Utilities.ActivityUtilities;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.CURRENT_USER_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_LATITUDE_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_LONGITUDE_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATION_TITLE_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.LOCATOR_ACTIVITY_REQUEST_CODE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MARKER_TITLE;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.RECIPIENT_NAME;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.SELECTED_LOCATION_KEY;

public class MailCompositionActivity extends AppCompatActivity {

    private static final String TAG = "MailCompositionActivity";

    private MailCompositionPresenter mMailCompositionPresenter;

    private User mCurrentUser;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_composition);

        mCurrentUser = new User();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentUser.setUsername(mSharedPreferences.getString(CURRENT_USER_KEY, ""));

        if (mCurrentUser.getUsername().contentEquals("")) {
            startActivity(new Intent(this, SignInActivity.class));
        }
        Intent intent = getIntent();

        MailCompositionFragment mailCompositionFragment =
                (MailCompositionFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mail_composition_fragment_frame);

        if (mailCompositionFragment == null) {
            mailCompositionFragment = MailCompositionFragment.newInstance();
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailCompositionFragment,
                    R.id.mail_composition_fragment_frame);
        }
        mMailCompositionPresenter = new MailCompositionPresenter(mailCompositionFragment, this);
        mMailCompositionPresenter.setCurrentUser(mCurrentUser);
        mMailCompositionPresenter.passIntent(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOCATOR_ACTIVITY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    double latitude = data.getDoubleExtra(LOCATION_LATITUDE_KEY, 0);
                    double longitude = data.getDoubleExtra(LOCATION_LONGITUDE_KEY, 0);
                    String markerTitle = data.getStringExtra(LOCATION_TITLE_KEY);
                    mMailCompositionPresenter.setLocation(latitude, longitude, markerTitle);
                } else {
                    mMailCompositionPresenter.setLocationCancelled();
                }
        }
    }
}