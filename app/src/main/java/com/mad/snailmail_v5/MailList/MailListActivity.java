package com.mad.snailmail_v5.MailList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mad.snailmail_v5.R;

import Model.User;
import Utilities.ActivityUtilities;

public class MailListActivity extends AppCompatActivity {

    private static final String TAG = "MailListActivity";
    private MailListPresenter mMailListPresenter;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);

        // ensure user is set
        if (mCurrentUser == null) {
            // startActivity(new Intent(this, SignInActivity.class));
        }

        // may need to restore current state through saved inst bundle

        MailListFragment mailListFragment =
                (MailListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mail_list_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailListFragment == null) {
            mailListFragment = mailListFragment.newInstance();
            // Log.d(TAG, "onCreate: " + mailListFragment.toString());
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailListFragment,
                    R.id.mail_list_fragment_frame);
        }

        mMailListPresenter = new MailListPresenter(mailListFragment);
    }
}
