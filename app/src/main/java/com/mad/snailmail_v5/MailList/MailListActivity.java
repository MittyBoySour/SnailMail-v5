package com.mad.snailmail_v5.MailList;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.snailmail_v5.R;

import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Utilities.ActivityUtilities;

// TODO: Make FirebaseManager abstract with separate implementations for activities
// TODO: Add HashMap (and maybe builders) for each com.mad.snailmail_v5.Model (mail, geo) [may not be necessary as cached]
// TODO: Add image storage

public class MailListActivity extends AppCompatActivity {

    Button mComposeMailButton;

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
            // remove this
            mCurrentUser = new User();
            mCurrentUser.setUsername("TestUser0");
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
        mMailListPresenter.setCurrentUser(mCurrentUser);
    }
}
