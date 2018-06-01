package com.mad.snailmail_v5.MailList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.snailmail_v5.R;

import java.util.ArrayList;
import java.util.List;

import Model.Mail;
import Model.User;
import Utilities.FirebaseManager;
import Utilities.MailAdapter;


public class MailListFragment extends Fragment implements MailListContract.View {

    private static final String TAG = "MailListFragment";

    private MailListContract.Presenter mPresenter;

    private RecyclerView mMailRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User mCurrentUser;
    private FirebaseManager mFirebaseManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign to the xml file

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // could potentially move this all to onCreate if no other frag/state will exist for activity

        View root = inflater.inflate(R.layout.mail_list_frag, container, false);

//        mCurrentUser = new User();
//        mCurrentUser.setUsername("TestUser0");

        mMailRecyclerView = root.findViewById(R.id.mail_list_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());// check for null
        mMailRecyclerView.setLayoutManager(mLayoutManager);

        mCurrentUser = new User();
        mCurrentUser.setUsername("TestUser0");

        mFirebaseManager = FirebaseManager.getInstance(mCurrentUser.getUsername(), mPresenter); // check notnull on presenter

        writeNewUser(mCurrentUser);

        setupTestTree();

        // need to swap this to presenter.getUserMailAdapter
        mMailRecyclerView.setAdapter(mFirebaseManager.getUserMailAdapter());

        return root;
    }

    @Override
    public void onPause() {
        // pause listening on adapter
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // resume listening on adapter
    }

    @Override
    public void setPresenter(MailListContract.Presenter presenter) {
        mPresenter = presenter; // add checkNotNull library
    }

    public static MailListFragment newInstance() {
        return new MailListFragment();
    }

    @Override
    public void newUserRegistered(String username) {
        mPresenter.addNewUser(username);
    }

    //////////////////// FAKE DB SETUP ////////////////

    private void setupTestTree() {
        User testUser0 = mCurrentUser;
        User testUser1 = new User();
        testUser1.setUsername("TestUser1");
        writeNewUser(testUser1);
        addUserToContacts(testUser0, testUser1, "TestFriend1");
        Mail mailFromUser0 = new Mail();
        mailFromUser0.setSender(testUser0.getUsername());
        mailFromUser0.setRecipient(testUser1.getUsername());
        mailFromUser0.setTitle("Mail 1 title");
        sendNewMailToUser(mailFromUser0);
        addUserToContacts(testUser1, testUser0, "TestFriend0");
        Mail mailFromUser1 = new Mail();
        mailFromUser1.setSender(testUser1.getUsername());
        mailFromUser1.setRecipient(testUser0.getUsername());
        mailFromUser1.setTitle("Re: Mail 1 title");
        sendNewMailToUser(mailFromUser1);
    }

    private void addUserToContacts(User user, User contact, String nickname) {
        mFirebaseManager.addContactForUser(user.getUsername(), contact.getUsername());
    }

    private void sendNewMailToUser(Mail mail) {
        mFirebaseManager.sendMailToContact(mail);
    }

    private void writeNewUser(User user) {
        // assumes check for userName validity has been performed
        // need to convert to map that saves everything at username location but not username
        mFirebaseManager.addNewUser(user.getUsername());

        // call toMap if further data needs to be added on creation of user
        // currently only username needs to be stored, which is the key.
    }

    @Override
    public void displayMailListFromAdapter() {
        mMailRecyclerView.setAdapter(mFirebaseManager.getUserMailAdapter());
    }
}
