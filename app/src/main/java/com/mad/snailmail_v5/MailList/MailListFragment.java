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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.snailmail_v5.R;

import java.util.List;

import Model.Mail;


public class MailListFragment extends Fragment implements MailListContract.View {

    private MailListContract.Presenter mPresenter;

    private FirebaseRecyclerAdapter mRecyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign to the xml file
        // initialise Firebase adapter
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.mail_list_frag, container, false);

        // set up Firebase Recycler Adapter

        return super.onCreateView(inflater, container, savedInstanceState); // return created view
    }

    @Override
    public void showMail(List<Mail> mailList) {

    }

    @Override
    public void setPresenter(MailListContract.Presenter presenter) {
        mPresenter = presenter; // add checkNotNull library
    }

    public static MailListFragment newInstance() {
        return new MailListFragment();
    }
}
