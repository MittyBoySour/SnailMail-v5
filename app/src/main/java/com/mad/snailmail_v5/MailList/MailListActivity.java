package com.mad.snailmail_v5.MailList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.snailmail_v5.MailComposition.MailCompositionActivity;
import com.mad.snailmail_v5.R;

import com.mad.snailmail_v5.Model.User;
import com.mad.snailmail_v5.Roaming.RoamingActivity;
import com.mad.snailmail_v5.Utilities.ActivityUtilities;

import static com.mad.snailmail_v5.Utilities.ActivityConstants.ActivityKeys.CURRENT_USER_KEY;

// TODO: Make FirebaseManager abstract with separate implementations for activities
// TODO: Add HashMap (and maybe builders) for each com.mad.snailmail_v5.Model (mail, geo) [may not be necessary as cached]
// TODO: Add image storage

public class MailListActivity extends AppCompatActivity {

    FloatingActionButton mComposeMailFAB;

    private static final String TAG = "MailListActivity";

    private MailListPresenter mMailListPresenter;
    private User mCurrentUser;
    private SharedPreferences mSharedPreferences;
    private Button mRoamingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_list);

        // ensure user is set
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mCurrentUser == null) {
            // startActivity(new Intent(this, SignInActivity.class));
            // remove this
            mCurrentUser = new User();
            mCurrentUser.setUsername("TestUser0");

            mSharedPreferences.edit().putString(CURRENT_USER_KEY, mCurrentUser.getUsername()).apply();

        }

        mComposeMailFAB = (FloatingActionButton) findViewById(R.id.compose_mail_fab);
        mComposeMailFAB.setOnClickListener(getFABClickListener());

        mRoamingButton = (Button) findViewById(R.id.start_roaming);
        mRoamingButton.setOnClickListener(getRoamingButtonClickListener());


        // may need to restore current state through saved inst bundle

        MailListFragment mailListFragment =
                (MailListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mail_list_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailListFragment == null) {
            mailListFragment = MailListFragment.newInstance();
            // Log.d(TAG, "onCreate: " + mailListFragment.toString());
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailListFragment,
                    R.id.mail_list_fragment_frame);
        }

        mMailListPresenter = new MailListPresenter(mailListFragment, this);
        mMailListPresenter.setCurrentUser(mCurrentUser);
    }

    private View.OnClickListener getFABClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MailListActivity.this,
                        MailCompositionActivity.class);
                intent.putExtra(CURRENT_USER_KEY, mCurrentUser.getUsername());
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener getRoamingButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MailListActivity.this, RoamingActivity.class));
            }
        };
    }

}
