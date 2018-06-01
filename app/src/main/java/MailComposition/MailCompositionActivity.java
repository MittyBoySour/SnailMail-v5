package MailComposition;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mad.snailmail_v5.R;

import Model.User;
import Utilities.ActivityUtilities;

public class MailCompositionActivity extends AppCompatActivity {

    private static final String TAG = "MailListActivity";
    private MailCompositionPresenter mMailCompositionPresenter;

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

        MailCompositionFragment mailListFragment =
                (MailCompositionFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mail_composition_fragment_frame);

        // TODO: may need to be moved to onAttachFragment()
        if (mailListFragment == null) {
            mailListFragment = mailListFragment.newInstance();
            // Log.d(TAG, "onCreate: " + mailListFragment.toString());
            ActivityUtilities.addFragmentToActivity(
                    getSupportFragmentManager(), mailListFragment,
                    R.id.mail_composition_fragment_frame);
        }

        mMailCompositionPresenter = new MailCompositionPresenter(mailListFragment);
        mMailCompositionPresenter.setCurrentUser(mCurrentUser);
    }
}