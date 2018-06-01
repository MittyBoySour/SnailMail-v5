package com.mad.snailmail_v5.MailComposition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mad.snailmail_v5.R;

import Model.User;
import Utilities.ActivityUtilities;

public class MailCompositionActivity extends AppCompatActivity {

    private static final String TAG = "MailCompositionActivity";
    private MailCompositionPresenter mMailCompositionPresenter;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_composition);

        // ensure user is set
        if (mCurrentUser == null) {
            // startActivity(new Intent(this, SignInActivity.class));
            // remove this
            mCurrentUser = new User();
            mCurrentUser.setUsername("TestUser0");
        }

        // may need to restore current state through saved inst bundle

        MailCompositionFragment mailCompositionFragment =
                (MailCompositionFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mail_composition_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailCompositionFragment == null) {
            mailCompositionFragment = mailCompositionFragment.newInstance();
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailCompositionFragment,
                    R.id.mail_composition_fragment_frame);
        }

        mMailCompositionPresenter = new MailCompositionPresenter(mailCompositionFragment);
        mMailCompositionPresenter.setCurrentUser(mCurrentUser);
    }
}