package com.mad.snailmail_v5.MailRead;

import com.mad.snailmail_v5.MailList.MailAdapter;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.FirebaseManager;

public class MailReadPresenter implements MailReadContract.Presenter {

    private final MailReadContract.View mMailReadView;
    private FirebaseManager mFirebaseManager;
    private User mCurrentUser;

    MailReadPresenter(MailReadContract.View mailListView) {
        // check for null
        this.mMailReadView = mailListView;
        mMailReadView.setPresenter(this);
        mFirebaseManager = FirebaseManager.getInstance();
        mFirebaseManager.setPresenter(this);
        // call firebase get user
    }


    @Override
    public void start() {

    }

    @Override
    public void updateMailAdapter(MailAdapter mailAdapter) {

    }

    @Override
    public void updateContactList() {

    }

    @Override
    public void receptionLocationLongClicked() {

    }

    @Override
    public void replyFABClicked() {

    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
        mFirebaseManager.setUser(currentUser);
    }
}
