package com.mad.snailmail_v5.MailRead;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.R;
import com.mad.snailmail_v5.Utilities.ActivityUtilities;

public class MailReadActivity extends AppCompatActivity {

    FloatingActionButton mReplyToMailFAB;

    private static final String TAG = "MailReadActivity";

    private MailReadPresenter mMailReadPresenter;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_read);

        mCurrentUser = new User();
        mCurrentUser.setUsername("TestUser0");

        mReplyToMailFAB = (FloatingActionButton) findViewById(R.id.compose_mail_fab);
        mReplyToMailFAB.setOnClickListener(getFABClickListener());

        // may need to restore current state through saved inst bundle

        MailReadFragment mailReadFragment =
                (MailReadFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mail_read_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailReadFragment == null) {
            mailReadFragment = MailReadFragment.newInstance();
            // Log.d(TAG, "onCreate: " + mailListFragment.toString());
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailReadFragment,
                    R.id.mail_read_fragment_frame);
        }

        mMailReadPresenter = new MailReadPresenter(mailReadFragment);
        mMailReadPresenter.setCurrentUser(mCurrentUser);
        mMailReadPresenter.passIntent(getIntent());
    }



    private View.OnClickListener getFABClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMailReadPresenter.replyFABClicked();
            }
        };
    }


}
