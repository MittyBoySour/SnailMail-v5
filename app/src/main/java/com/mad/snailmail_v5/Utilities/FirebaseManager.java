package com.mad.snailmail_v5.Utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.snailmail_v5.MailList.MailAdapter;

import java.util.ArrayList;
import java.util.List;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

// maybe user Singleton approach
// create an interface

// may need to split into multiple implementations for various database refs
// this class should only consider the context/activity from which it is called
// can track from a switch statement where activity as int is passed in
// then design queries/parser/adapter based on that

// using retrofit, gson, and firebase
// http://sushildlh-retro-firebase.blogspot.com/

public class FirebaseManager<P extends BasePresenter> {

    // maybe move to constants file
    private static final String TAG = "FirebaseManager";
    // change to plurals
    private static final String USER_FILTER = "user";
    private static final String USER_MAIL_FILTER = "mail";
    private static final String USER_CONTACT_FILTER = "contact";

    private static DatabaseReference mRootDatabaseReference;

    private static FirebaseManager sInstance;

    private static ArrayList<String> mUsernameList;
    private static ArrayList<String> mUserContactList;

    private P mPresenter;
    private User mCurrentUser;

    private FirebaseManager() {
        mRootDatabaseReference = FirebaseDatabase.getInstance()
                .getReference();
        mUsernameList = new ArrayList<>();
        sendUsernameListRequest();
    }

    public static FirebaseManager getInstance() {
        if (sInstance == null) {
            sInstance = new FirebaseManager();
        }
        Log.d(TAG, "getInstance: " + sInstance.toString());
        return sInstance;
    }

    public void setUser(User user) {
        mCurrentUser = user;
        sendUserMailListRequest(user.getUsername());
    }

    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    public void updateMailList() {
        sendUserMailListRequest(mCurrentUser.getUsername());
    }

    ////////////// CURRENT DB LIST //////////////

    private void sendUsernameListRequest() {
        getUserListReference().addListenerForSingleValueEvent(getUsernameListResponseListener());
    }

    private void sendUserMailListRequest(String username) {
        getUserMailListReference(username).addListenerForSingleValueEvent(
                getUserMailListResponseListener());
    }

    private void sendContactListRequest(String username) {
        getUserContactListReference(username).addListenerForSingleValueEvent(
                getUserContactListResponseListener());
    }

    // maybe make some of these generic with enums from consts/ints to pass in for diff response

    ////////////// DB REFERENCES ////////////////

    // probably convert to path builder

    private DatabaseReference getUserMailReference(String username, String mailKey) {
        return mRootDatabaseReference.child(USER_FILTER)
                .child(username).child(USER_MAIL_FILTER).child(mailKey);
    }

    private DatabaseReference getUserMailListReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER)
                .child(username).child(USER_MAIL_FILTER);
    }

    private DatabaseReference getUserReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER).child(username);
    }

    private DatabaseReference getUserListReference() {
        return mRootDatabaseReference.child(USER_FILTER);
    }

    private DatabaseReference getUserContactListReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER).child(username)
                .child(USER_CONTACT_FILTER);
    }

    ////////////// FIREBASE RESPONSE LISTENERS ////////////////

    private ValueEventListener getUserMailListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear the current list and add new items to it
                // call presenter with new data
                // create new MailAdapter with new list data
                // call presenter to update
                // presenter will call view to update passing it new adapter
                ArrayList<Mail> userMailList = new ArrayList<>();
                Mail mail;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    mail = snapshotChild.getValue(Mail.class);
                    // check for collected status
                    userMailList.add(mail);
                }
                mPresenter.updateMailAdapter(new MailAdapter(userMailList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // clear the current list and add new items to it
                // call presenter with new data
                // list of contacts to verify against for various methods
            }
        };
    }

    private ValueEventListener getUsernameListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mUserMailList.clear();
//                Mail mail;
//                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
//                    mail = snapshotChild.getValue(Mail.class);
//                    mUserMailList.add(mail);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private ValueEventListener getUserContactListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserContactList.clear();
                String contactUsername;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    // check boolean for still true later when remove contact is enabled
                    contactUsername = snapshotChild.getKey();
                    mUserContactList.add(contactUsername);
                }
                // add contact spinner adapter population here
                mPresenter.updateContactList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }


    ////////////// FIREBASE INTERACTION METHODS ///////////////

    public void addNewUser(String username) {
        // validate name
        getUserListReference().child(username);
    }

    // may need to return string
    public void sendMailToContact(Mail mail) {
        if (mail.getRecipient().equalsIgnoreCase("")) { return; } // with error
        // add listener to checking whether user exists
        String recipient = mail.getRecipient();
        String mailKey = getUserMailListReference(recipient).push().getKey();
        getUserMailReference(recipient, mailKey).setValue(mail); // check that user still exists first

    }

    // later will make contact separate class, if adding nickname possibility

    public void addContactForUser(String username, String contactName) {
        getUserReference(username).child("contact").child(contactName).setValue(true);
        sendContactListRequest(username);
                // add on success listener instead
                // .addListenerForSingleValueEvent(getUserContactsListener());
    }

    // will add removeContact also that just sets value to false

    public List<String> getContactListForUser(String username) {
        return mUserContactList;
    }

    public void getMailListForUser(String username) {
        getUserMailListReference(username).addListenerForSingleValueEvent(
                getUserMailListResponseListener());
    }

    public Mail getMailForUser(String username, String mailId) {
        // possibly try using a separate thread to await the data change
        // may need context of what called the mail, to update the view
        return new Mail();
    }

    public boolean userExists(String username) {
        return (getUserListReference().child(username).getKey() != null);
    }

}
