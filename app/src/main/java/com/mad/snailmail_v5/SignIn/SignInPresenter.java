package com.mad.snailmail_v5.SignIn;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.mad.snailmail_v5.MailList.MailListActivity;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.CURRENT_USER_KEY;

public class SignInPresenter implements SignInContract.Presenter {

    private static boolean mSigninAttempt;
    private final SignInContract.View mSignInView;
    private final Activity mActivityContext;
    private FirebaseManager mFirebaseManager;
    private SharedPreferences mSharedPreferences;
    private String mUsername;

    public SignInPresenter(SignInContract.View view, Activity context) {
        this.mSignInView = view;
        mSignInView.setPresenter(this);
        mActivityContext = context;
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
    }

    public void UserSignInAttempt(String username) {
        mSigninAttempt = true;
        mSignInView.setDataLoading(true);
        mUsername = username;
        mFirebaseManager.verifyUser(username);
    }

    public void UserRegisterAttempt(String username) {
        mSigninAttempt = false;
        mSignInView.setDataLoading(true);
        mUsername = username;
        mFirebaseManager.verifyUser(username);
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

    @Override
    public void setCurrentUser(User user) {

    }

    @Override
    public void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList) {

    }

    @Override
    public void userListResponse() {

    }


    @Override
    public void userExistenceResponse(boolean userExists) {
        if (userExists) {
            if (mSigninAttempt) {
                // sign in attempt successful, store name and start mail list activity
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivityContext);
                mSharedPreferences.edit().putString(CURRENT_USER_KEY, mUsername).apply();
                Intent intent = new Intent(mActivityContext.getApplication(), MailListActivity.class);
            } else {
                // register attempt unsuccessful
                mSignInView.displayToast(Toast.makeText(
                        mActivityContext, R.string.user_exists_toast, Toast.LENGTH_LONG
                ));
            }
        } else {
            // sign in attempt unsuccessful
            if (mSigninAttempt) {
                mSignInView.displayToast(Toast.makeText(
                        mActivityContext, R.string.user_does_not_exists_toast, Toast.LENGTH_LONG
                ));

            } else {
                // register attempt successful, store name and start mail list activity
                mFirebaseManager.addNewUser(mUsername);
                mSharedPreferences.edit().putString(CURRENT_USER_KEY, mUsername).apply();
            }
        }
    }

    @Override
    public void userSuccessfullyAdded() {
        mSignInView.setDataLoading(false);
        Intent intent = new Intent(mActivityContext.getApplication(), MailListActivity.class);
    }
}
