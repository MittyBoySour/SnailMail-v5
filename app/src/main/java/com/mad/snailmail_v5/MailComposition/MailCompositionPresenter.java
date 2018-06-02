package com.mad.snailmail_v5.MailComposition;

import android.support.annotation.NonNull;

import com.mad.snailmail_v5.MailList.MailAdapter;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

class MailCompositionPresenter implements MailCompositionContract.Presenter {

    private enum ViewOption {
        MAP, MESSAGE
    }

    private final MailCompositionContract.View mMailCompositionView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailCompositionPresenter(MailCompositionContract.View mailCompositionView) {
        // check for null
        this.mMailCompositionView = mailCompositionView;
        mMailCompositionView.setPresenter(this);
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
    }

    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager.setUser(mCurrentUser);
    }

    @Override
    public void submitMailButtonClicked(String title, String recipient, String message) {
        Mail mail = new Mail.Builder()
                .setSender(mCurrentUser.getUsername())
                .setRecipient(recipient)
                .setTitle(title)
                .setMessage(message)
                .build();
        mFirebaseManager.sendMailToContact(mail);
    }

    @Override
    public void discardMailButtonClicked() {
        // maybe show confirmation dialogue first
        // start mail list activity
    }

    @Override
    public void DeliveryLocationLongClicked(boolean displayingMap) {
        // options: 0 - show mapView, 1 - show Message
        if (displayingMap) {
            mMailCompositionView.showMiddleView(0);
        } else {
            mMailCompositionView.showMiddleView(1);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void updateMailAdapter(MailAdapter mailAdapter) {

    }

    @Override
    public void updateContactList() {
        // set up spinner adapter here
        // pass spinner adapter to contact list dropdown
    }

}
