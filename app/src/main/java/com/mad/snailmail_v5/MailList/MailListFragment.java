package com.mad.snailmail_v5.MailList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.R;

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

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.mailListRefreshRequested();
    }

    @Override
    public void setPresenter(MailListContract.Presenter presenter) {
        mPresenter = presenter; // add checkNotNull library
    }

    @Override
    public void displayDialogue(AlertDialog dialogue) {

    }

    @Override
    public void requestPermissions(String[] permissions) {

    }

    @Override
    public void displayToast(Toast toast) {

    }

    public static MailListFragment newInstance() {
        return new MailListFragment();
    }

    @Override
    public void attachMailAdapter(MailAdapter mailAdapter) {
        mMailRecyclerView.setAdapter(mailAdapter);
    }

}
