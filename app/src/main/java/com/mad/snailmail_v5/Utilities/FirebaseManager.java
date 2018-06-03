package com.mad.snailmail_v5.Utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.Model.BareGeofence;
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
    private static final String USER_BARE_GEOFENCE_FILTER = "bareGeofence";

    private static DatabaseReference mRootDatabaseReference;

    private static FirebaseManager sInstance;
    private static DeliveryManager mDeliveryManager;

    private static ArrayList<String> mUsernameList;
    private static ArrayList<String> mUserContactList;

    private P mPresenter;
    private User mCurrentUser;

    private FirebaseManager() {
        mRootDatabaseReference = FirebaseDatabase.getInstance()
                .getReference();
        mUsernameList = new ArrayList<>();
        sendUsernameListRequest();
        mDeliveryManager = DeliveryManager.getInstance();
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

    private DatabaseReference getUserGeofenceListReference(String username) {
        return mRootDatabaseReference.child(USER_FILTER).child(username)
                .child(USER_BARE_GEOFENCE_FILTER);
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
                ArrayList<String> userMailKeyList = new ArrayList<>();
                Mail mail;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    mail = snapshotChild.getValue(Mail.class);
                    // check for delivery status
                    if (mDeliveryManager.mailDelivered(mail)) {
                        mail.setDelivered(true);
                        userMailKeyList.add(snapshotChild.getKey());
                    }
                    userMailList.add(mail);
                }
                writeMailListBackToFirebase(userMailKeyList);
                mPresenter.userMailListResponse(userMailList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // clear the current list and add new items to it
                // call presenter with new data
                // list of contacts to verify against for various methods
            }
        };
    }

    private ValueEventListener getUserMailUpdateResponseListener(final ArrayList<String> userMailKeyList) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mailKey;
                Mail mail;
                HashMap<String, Object> mailListUpdates = new HashMap<>();
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {

                    mailKey = snapshotChild.getKey();
                    mail = snapshotChild.getValue(Mail.class);
                    // set active status
                    for (String collectedMailKey : userMailKeyList) {
                        if (mailKey.contentEquals(collectedMailKey)) {
                            mail.setCollected(false);
                            mailListUpdates.put(mailKey, mail);
                        }
                    }
                }
                getUserMailListReference(mCurrentUser.getUsername()).updateChildren(mailListUpdates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private ValueEventListener getUsernameListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> usernameList = new ArrayList<>();
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    usernameList.add(snapshotChild.getKey());
                }
                mPresenter.userListResponse();
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
                ArrayList<String> UserContactList = new ArrayList<>();
                String contactUsername;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    // check boolean for still true later when remove contact is enabled
                    contactUsername = snapshotChild.getKey();
                    UserContactList.add(contactUsername);
                }
                // add contact spinner adapter population here
                mPresenter.updateUserContactList(UserContactList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private ValueEventListener getUserGeofenceListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<BareGeofence> bareGeofenceList = new ArrayList<>();
                String geofenceReferenceKey;
                BareGeofence bareGeofence;
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {

                    geofenceReferenceKey = snapshotChild.getKey();
                    bareGeofence = snapshotChild.getValue(BareGeofence.class);
                    // check for active status
                    if (!bareGeofence.isActive()) {
                        bareGeofence.setGeofenceReferenceKey(geofenceReferenceKey);
                        bareGeofenceList.add(bareGeofence);
                    }
                }
                // add contact spinner adapter population here
                mPresenter.bareGeofenceResponse(bareGeofenceList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private ValueEventListener getUserGeofenceListUpdateResponseListener(final ArrayList<String> triggeredGeofences) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String geofenceReferenceKey;
                BareGeofence bareGeofence;
                HashMap<String, Object> bareGeoUpdates = new HashMap<>();
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {

                    geofenceReferenceKey = snapshotChild.getKey();
                    bareGeofence = snapshotChild.getValue(BareGeofence.class);
                    // set active status
                    for (String triggeredKey : triggeredGeofences) {
                        if (triggeredKey.contentEquals(geofenceReferenceKey)) {
                            bareGeofence.setActive(false);
                            bareGeoUpdates.put(triggeredKey, bareGeofence);
                        }
                    }
                }
                getUserGeofenceListReference(mCurrentUser.getUsername()).updateChildren(bareGeoUpdates)
                        .addOnCompleteListener(getGeofenceUpdateCompleteListener(triggeredGeofences));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private OnCompleteListener<Void> getGeofenceUpdateCompleteListener(final ArrayList<String> triggeredGeofences) {
        return new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // check success
                updateMailGeofenceCollected(triggeredGeofences);
            }
        };
    }

    private OnCompleteListener<Void> getMailSentListener(final Mail mail) {
        return new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // check whether successful and pass back to store locally if not

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
        // should check and wait for success

        String recipient = mail.getRecipient();
        String mailKey = getUserMailListReference(recipient).push().getKey();
        String geofenceKey = getUserReference(recipient).child(USER_BARE_GEOFENCE_FILTER)
                .push().getKey();

        boolean isActive = false;
        if (!mail.isCollected() && mail.isDelivered()) isActive = true;
        HashMap<String, Object> bareGeofence = new BareGeofence(isActive,
                mail.getDestinationLatitude(), mail.getDestinationLongitude()).toHashMap();

        getUserReference(recipient).child(USER_BARE_GEOFENCE_FILTER).child(geofenceKey)
                .updateChildren(bareGeofence);

//        bareGeorefence.put();
//        double latitude = mail.getCoordinates()[2];
//        double longitude = mail.getCoordinates()[3];
//
//        getUserReference(recipient).child(USER_BARE_GEOFENCE_FILTER).child(geofenceKey)
//                .child(BARE_GEOFENCE_COLLECTED_STATUS_STORAGE_KEY).setValue(latitude);
//        getUserReference(recipient).child(USER_BARE_GEOFENCE_FILTER).child(geofenceKey)
//                .child(BARE_GEOFENCE_LATITUDE_STORAGE_KEY).setValue(latitude);
//        getUserReference(recipient).child(USER_BARE_GEOFENCE_FILTER).child(geofenceKey)
//                .child(BARE_GEOFENCE_LONGITUDE_STORAGE_KEY).setValue(longitude);

        mail.setGeofenceReferenceKey(geofenceKey);

        getUserMailReference(recipient, mailKey).setValue(mail)
                .addOnCompleteListener(getMailSentListener(mail));

    }

    // later will make contact separate class, if adding nickname possibility

    public void addContactForUser(String username, String contactName) {
        getUserReference(username).child(USER_CONTACT_FILTER).child(contactName).setValue(true);
        sendContactListRequest(username);
                // add on success listener instead
                // .addListenerForSingleValueEvent(getUserContactsListener());
    }

    public boolean userExists(String username) {
        return (getUserListReference().child(username).getKey() != null);
    }

    public void updateGeofences() {
        getUserGeofenceListReference(mCurrentUser.getUsername())
                .addListenerForSingleValueEvent(getUserGeofenceListResponseListener());
    }

    public void updateTriggeredGeofences(ArrayList<String> triggeredGeofences) {
        getUserGeofenceListReference(mCurrentUser.getUsername())
                .addListenerForSingleValueEvent(getUserGeofenceListUpdateResponseListener(triggeredGeofences));
    }

    public void updateMailGeofenceCollected(ArrayList<String> triggeredGeofences) {

    }

    private void writeMailListBackToFirebase(ArrayList<String> userMailKeyList) {
        getUserMailListReference(mCurrentUser.getUsername())
                .addListenerForSingleValueEvent(getUserMailUpdateResponseListener(userMailKeyList));
    }

}
