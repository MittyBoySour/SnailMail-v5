package com.mad.snailmail_v5.MailList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.snailmail_v5.R;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

public class MailListFragment extends Fragment implements MailListContract.View {

    private static final String TAG = "MailListFragment";

    private MailListContract.Presenter mPresenter;

    private RecyclerView mMailRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mMailRecyclerView = root.findViewById(R.id.mail_list_recycler);
        mLayoutManager = new LinearLayoutManager(getActivity());// check for null
        mMailRecyclerView.setLayoutManager(mLayoutManager);
        // get rid of eventually
        runSeriesOfTestActions();

        displayMailListFromAdapter();

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
    public void registerNewUserButtonClicked(String username) {
        mPresenter.addNewUser(username);
    }

    @Override
    public void addNewContactButtonClicked(String contactUsername) {
        mPresenter.addContactForUser(contactUsername);
    }

    public void submitMailButtonClicked(Mail mail) {
        // compose the mail from the fields in onClick
        mPresenter.sendMailToContact(mail);
    }

    //////////////////// FAKE DB SETUP ////////////////

    private void runSeriesOfTestActions() {
        // user0 actions
        User testUser0 = new User();
        testUser0.setUsername("TestUser0");
        registerNewUserButtonClicked(testUser0.getUsername());
        // user1 actions
        User testUser1 = new User();
        testUser1.setUsername("TestUser1");
        registerNewUserButtonClicked(testUser1.getUsername());
        // user0 actions
        addNewContactButtonClicked(testUser1.getUsername());
        Mail mailFromUser0 = new Mail();
        mailFromUser0.setSender(testUser0.getUsername());
        mailFromUser0.setRecipient(testUser1.getUsername());
        mailFromUser0.setTitle("Mail 1 title");
        submitMailButtonClicked(mailFromUser0);
        // user1 actions
        addNewContactButtonClicked(testUser0.getUsername());
        Mail mailFromUser1 = new Mail();
        mailFromUser1.setSender(testUser1.getUsername());
        mailFromUser1.setRecipient(testUser0.getUsername());
        mailFromUser1.setTitle("Re: Mail 1 title");
        submitMailButtonClicked(mailFromUser1);
    }

    @Override
    public void displayMailListFromAdapter() {
        mMailRecyclerView.setAdapter(mPresenter.getUserMailAdapter());
    }
}
