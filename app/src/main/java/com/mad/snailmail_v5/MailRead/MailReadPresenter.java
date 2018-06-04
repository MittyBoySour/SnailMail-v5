package com.mad.snailmail_v5.MailRead;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mad.snailmail_v5.MailComposition.MailCompositionActivity;
import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

import java.util.ArrayList;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.MAIL_ITEM_KEY;
import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.RECIPIENT_NAME;

public class MailReadPresenter implements MailReadContract.Presenter {

    private final MailReadContract.View mMailReadView;
    private final Activity mActivityContext;
    private User mCurrentUser;
    private Mail mMail;

    MailReadPresenter(MailReadContract.View mailListView, Activity context) {
        // check for null
        this.mMailReadView = mailListView;
        mMailReadView.setPresenter(this);
        mActivityContext = context;
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
        Intent intent = new Intent(mActivityContext.getApplication(), MailCompositionActivity.class);
        intent.putExtra(RECIPIENT_NAME, mMail.getRecipient());
        mActivityContext.startActivity(intent);
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

    @Override
    public void userExistenceResponse(boolean userExists) {

    }

    @Override
    public void userSuccessfullyAdded() {

    }

    public void passIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        Mail mail = (Mail) bundle.getParcelable(MAIL_ITEM_KEY);
        mMail = mail;
        mMailReadView.setMail(mail);
    }
}
