package com.mad.snailmail_v5.MailList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mad.snailmail_v5.MailRead.MailReadActivity;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_ITEM_KEY;

class MailListPresenter implements MailListContract.Presenter {

    private final MailListContract.View mMailListView;
    private final Context mContext;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailListPresenter(MailListContract.View mailListView, Context context) {
        // check for null
        mMailListView = mailListView;
        mContext = context;
        mMailListView.setPresenter(this);
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
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
    public void userMailListResponse(ArrayList<Mail> mailList) {
        mMailListView.attachMailAdapter(new MailAdapter(mailList, this));
    }

    @Override
    public void updateUserContactList(ArrayList<String> UserContactList) {

    }

    @Override
    public void addNewUser(String username) {
        mFirebaseManager.addNewUser(username);
    }

    @Override
    public void mailListRefreshRequested() {
        mFirebaseManager.updateMailList();
    }

    @Override
    public void mailTitleClicked(Mail mail) {
        if (mail.isDelivered() && mail.isCollected()) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(MAIL_ITEM_KEY, mail);
            intent.putExtras(bundle);
            intent.setClass(((Activity) mContext), MailReadActivity.class);
            mContext.startActivity(intent);
        }
        mMailListView.displayToast(Toast.makeText(
                mContext, R.string.mail_unavailable, Toast.LENGTH_SHORT
        ));
    }

    //////////////// NOT NEEDED ///////////////////

    @Override
    public void start() {

    }

    @Override
    public void permissionsNotSet(boolean firstRequest, String[] permissions) {

    }

}
