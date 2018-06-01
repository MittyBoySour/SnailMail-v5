package com.mad.snailmail_v5.MailComposition;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

class MailCompositionPresenter implements MailCompositionContract.Presenter {

    private final MailCompositionContract.View mMailCompositionView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailCompositionPresenter(MailCompositionContract.View mailCompositionView) {
        // check for null
        this.mMailCompositionView = mailCompositionView;

        mMailCompositionView.setPresenter(this);
    }

    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager = FirebaseManager.getInstance(mCurrentUser.getUsername(), this);
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
    public void start() {

    }

    @Override
    public void updateMailList() {

    }
}
