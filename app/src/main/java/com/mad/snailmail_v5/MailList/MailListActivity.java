package com.mad.snailmail_v5.MailList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mad.snailmail_v5.R;

import Utilities.ActivityUtilities;

public class MailListActivity extends AppCompatActivity {

    private MailListPresenter mMailListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);

        // may need to restore current state through saved inst bundle

        MailListFragment mailListFragment =
                (MailListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mail_list_fragment_frame);

        if (mailListFragment == null) {
            mailListFragment = mailListFragment.newInstance();
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailListFragment,
                    R.id.mail_list_fragment_frame);
        }

        mMailListPresenter = new MailListPresenter(mailListFragment);
    }
}
