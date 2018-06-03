package com.mad.snailmail_v5.MailRead;

import android.content.Intent;
import android.os.Bundle;

import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_ITEM_KEY;

public class MailReadPresenter implements MailReadContract.Presenter {

    private final MailReadContract.View mMailReadView;
    private User mCurrentUser;

    MailReadPresenter(MailReadContract.View mailListView) {
        // check for null
        this.mMailReadView = mailListView;
        mMailReadView.setPresenter(this);
        // call firebase get user
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
    public void receptionLocationLongClicked() {

    }

    @Override
    public void replyFABClicked() {

    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    @Override
    public void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList) {

    }

    @Override
    public void userListResponse() {

    }

    public void passIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        Mail mail = (Mail) bundle.getParcelable(MAIL_ITEM_KEY);
        mMailReadView.setMail(mail);
    }
}
