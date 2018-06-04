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

public class FirebaseManager<P extends BasePresenter> {


    private static final String TAG = "FirebaseManager";

    // JSON database path tree constants
    private static final String USER_FILTER = "user";
    private static final String USER_MAIL_FILTER = "mail";
    private static final String USER_CONTACT_FILTER = "contact";
    private static final String USER_BARE_GEOFENCE_FILTER = "bareGeofence";

    private static DatabaseReference mRootDatabaseReference;

    private static FirebaseManager sInstance;
    private static DeliveryManager mDeliveryManager;

    private P mPresenter;
    private User mCurrentUser;

    private FirebaseManager() {
        mRootDatabaseReference = FirebaseDatabase.getInstance()
                .getReference();
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


    /**
     * below are a set of requests to the Database that trigger asynchonous listeners to update the
     * the presenter.
     *
     * All are named in such a way that the path through the JSON tree should be clear
     * (i.e the path trees track left to right)
     *
     * The parameters provided are keys to track further into the tree for the appropriate item
     */
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

    ////////////// DB REFERENCES ////////////////


    /**
     * below are a set of path builders to provide concrete references to the Database.
     *
     * All are named in such a way that the path through the JSON tree should be clear
     * (i.e the path trees track left to right)
     *
     * The parameters provided are keys to track further into the tree for the appropriate item
     */
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

    /**
     * Below are the methods used to asynchronously listen and respond to database calls.
     * They are each crafted for a specific database reference or action to handle
     *
     * If the methods take parameters, it is so that they cn perform operations on the database
     *
     */

    /**
     * Iterates through the current mail list for the user and calls the current presenter
     * when completed to the mail list as required (through the adapter)
     */
    private ValueEventListener getUserMailListResponseListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                Log.d(TAG, "onCancelled: " + databaseError.getCode());
            }
        };
    }

    /**
     * iterates through the list of user mail and updates any mail items from the passed
     * mail item list to notify it that those items have changed
     *
     * @param userMailKeyList
     */
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
                Log.d(TAG, "onCancelled: " + databaseError.getCode());

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
                Log.d(TAG, "onCancelled: " + databaseError.getCode());

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
                Log.d(TAG, "onCancelled: " + databaseError.getCode());

            }
        };
    }

    /**
     * iterates through the list of geofences and updates any triggered from the passed
     * geofence item list to notify it that those items have changed
     *
     * @param triggeredGeofences
     */
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
                Log.d(TAG, "onCancelled: " + databaseError.getCode());

            }
        };
    }


    /**
     * iterates through the list of geofences and updates any items from the passed
     * mail item list to notify it that those items have changed
     *
     * @param triggeredGeofences
     */
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
        getUserListReference().child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPresenter.userSuccessfullyAdded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        mail.setGeofenceReferenceKey(geofenceKey);

        getUserMailReference(recipient, mailKey).setValue(mail)
                .addOnCompleteListener(getMailSentListener(mail));

    }

    public void addContactForUser(String username, String contactName) {
        getUserReference(username).child(USER_CONTACT_FILTER).child(contactName).setValue(true);
        sendContactListRequest(username);
    }

    private boolean userExists(String username) {
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

    public void verifyUser(String username) {
        mPresenter.userExistenceResponse(userExists(username));
    }

}
