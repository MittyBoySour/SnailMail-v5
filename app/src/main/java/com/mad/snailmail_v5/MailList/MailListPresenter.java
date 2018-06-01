package com.mad.snailmail_v5.MailList;

import android.support.v7.widget.RecyclerView;

import Model.Mail;
import Model.User;
import Utilities.FirebaseManager;

class MailListPresenter implements MailListContract.Presenter {

    private final MailListContract.View mMailListView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailListPresenter(MailListContract.View mailListView) {
        // check for null
        this.mMailListView = mailListView;

        mMailListView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setCurrentUser(User user) {
        mCurrentUser = user;
        mFirebaseManager = FirebaseManager.getInstance(mCurrentUser.getUsername(), this);
    }

    @Override
    public void updateMailList() {
        mMailListView.displayMailListFromAdapter();
    }

    @Override
    public void addNewUser(String username) {
        mFirebaseManager.addNewUser(username);
    }

    @Override
    public void addContactForUser(String contactUsername) {
        mFirebaseManager.addContactForUser(mCurrentUser.getUsername(), contactUsername);
    }

    @Override
    public RecyclerView.Adapter getUserMailAdapter() {
        return mFirebaseManager.getUserMailAdapter();
    }

    @Override
    public void sendMailToContact(Mail mail) {
        mFirebaseManager.sendMailToContact(mail);
    }
}
