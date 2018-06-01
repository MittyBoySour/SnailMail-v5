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

public class FirebaseManager {

    // maybe move to constants file
    private static final String TAG = "FirebaseManager";
    private static final String USER_FILTER = "user";
    private static final String USER_MAIL_FILTER = "mail";

    private static DatabaseReference mRootDatabaseReference;

    private static FirebaseManager sInstance;
    private static MailAdapter mMailAdapter;

    private static ArrayList<Mail> mUserMailList;
    private static ArrayList<User> mUserList;

    private static BasePresenter mPresenter;

    private FirebaseManager(String username, BasePresenter presenter) {
        Log.d(TAG, "FirebaseManager: private construct called");
        mRootDatabaseReference = FirebaseDatabase.getInstance()
                .getReference();

        mPresenter = presenter;

        setUserList();
        setUserMailList(username);

        mUserList = new ArrayList<>();
        mUserMailList = new ArrayList<>();
        mMailAdapter = new MailAdapter(mUserMailList);
    }

    // may need context

    public static FirebaseManager getInstance(String username, BasePresenter presenter) {

        if (sInstance == null) {
            Log.d(TAG, "getInstance: pre instance setup");

            sInstance = new FirebaseManager(username, presenter);
            Log.d(TAG, "getInstance: post instance setup");

        }
        Log.d(TAG, "getInstance: " + sInstance.toString());
        return sInstance;
    }

    public MailAdapter getUserMailAdapter() {
        return mMailAdapter;
    }

    ////////////// CURRENT DB LIST //////////////

    private static void setUserList() {
        getUserListReference().addListenerForSingleValueEvent(getUserListResponseListener());
    }

    private static void setUserMailList(String username) {
        getUserMailListReference(username).addListenerForSingleValueEvent(
                getUserMailListResponseListener());
    }

    // maybe make some of these generic with enums from consts/ints to pass in for diff response

    ////////////// DB REFERENCES ////////////////

    // probably convert to path builder

    private static DatabaseReference getUserMailReference(String username, String mailKey) {
        return mRootDatabaseReference.child(USER_FILTER)
                .child(username).child(USER_MAIL_FILTER).child(mailKey);
    }


    private static DatabaseReference getUserMailListReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER)
                .child(username).child(USER_MAIL_FILTER);
    }

    private static DatabaseReference getUserReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER).child(username);
    }

    private static DatabaseReference getUserListReference() {
        return mRootDatabaseReference.child(USER_FILTER);
    }

    ////////////// FIREBASE RESPONSE LISTENERS ////////////////

    private static ValueEventListener getUserMailListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear the current list and add new items to it
                // call presenter with new data
                // create new MailAdapter with new list data
                // call presenter to update
                // presenter will call view to update passing it new adapter
                mUserMailList.clear();
                Mail mail;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    mail = snapshotChild.getValue(Mail.class);
                    mUserMailList.add(mail);
                }
                mMailAdapter = new MailAdapter(mUserMailList);
                mPresenter.updateMailList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // clear the current list and add new items to it
                // call presenter with new data
                // list of contacts to verify against for various methods
            }
        };
    }

    private static ValueEventListener getUserListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private ValueEventListener getUserContactListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
        // consider whether listener is necessary
        getUserReference(username).child("contact").child(contactName);

        // check whether actually implementing the user list listener resolves this first
        // (WON'T WORK)
        // .addListenerForSingleValueEvent(getUserContactListener())
    }

    // will add removeContact also that just sets value to false

    public List<User> getContactListForUser(String username) {
        return new ArrayList<>();
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
