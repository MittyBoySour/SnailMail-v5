package com.mad.snailmail_v5.MailComposition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mad.snailmail_v5.R;

import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.ActivityUtilities;

import static com.mad.snailmail_v5.Constants.ActivityConstants.ActivityKeys.CURRENT_USER_KEY;

public class MailCompositionActivity extends AppCompatActivity {

    private static final String TAG = "MailCompositionActivity";

    private MailCompositionPresenter mMailCompositionPresenter;

    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_composition);

        // ensure user is set
        Intent intent = getIntent();
        mCurrentUser = new User();
        mCurrentUser.setUsername(intent.getStringExtra(CURRENT_USER_KEY));

        // may need to restore current state through saved inst bundle

        MailCompositionFragment mailCompositionFragment =
                (MailCompositionFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mail_composition_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailCompositionFragment == null) {
            mailCompositionFragment = MailCompositionFragment.newInstance();
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailCompositionFragment,
                    R.id.mail_composition_fragment_frame);
        }

        mMailCompositionPresenter = new MailCompositionPresenter(mailCompositionFragment);
        mMailCompositionPresenter.setCurrentUser(mCurrentUser);
    }
}