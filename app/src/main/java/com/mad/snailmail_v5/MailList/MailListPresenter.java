package com.mad.snailmail_v5.MailList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

class MailListPresenter implements MailListContract.Presenter {

    private final MailListContract.View mMailListView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailListPresenter(MailListContract.View mailListView) {
        // check for null
        this.mMailListView = mailListView;
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
    public void updateMailAdapter(MailAdapter mailAdapter) {
        mMailListView.attachMailAdapter(mailAdapter);
    }

    @Override
    public void addNewUser(String username) {
        mFirebaseManager.addNewUser(username);
    }

    @Override
    public void mailListRefreshRequested() {
        mFirebaseManager.updateMailList();
    }

    //////////////// NOT NEEDED ///////////////////

    @Override
    public void start() {

    }

    @Override
    public void updateContactList() {

    }

}
