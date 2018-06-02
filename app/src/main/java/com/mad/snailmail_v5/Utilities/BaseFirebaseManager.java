package com.mad.snailmail_v5.Utilities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.Model.User;

public class BaseFirebaseManager<P extends BasePresenter> {

    private static DatabaseReference mRootDatabaseReference;

    private static final String USER_FILTER = "user";
    private static final String USER_MAIL_FILTER = "mail";

    private P mPresenter;
    private User mCurrentUser;

    public BaseFirebaseManager() {
        mRootDatabaseReference = FirebaseDatabase.getInstance()
                .getReference();
    }

    private void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    private void setUser(User user) {
        mCurrentUser = user;
    }

    private DatabaseReference getUserListReference() {
        return mRootDatabaseReference.child(USER_FILTER);
    }

}
