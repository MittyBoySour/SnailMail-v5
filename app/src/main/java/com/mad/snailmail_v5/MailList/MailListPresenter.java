package com.mad.snailmail_v5.MailList;

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
        mFirebaseManager = FirebaseManager.getInstance(mCurrentUser.getUsername(), this);
    }

    @Override
    public void start() {

    }

    @Override
    public void updateMailList() {
        mMailListView.displayMailListFromAdapter();
    }

    @Override
    public void addNewUser(String username) {
        mFirebaseManager.addNewUser(username);
    }
}
