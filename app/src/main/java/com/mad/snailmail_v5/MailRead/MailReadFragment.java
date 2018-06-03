package com.mad.snailmail_v5.MailRead;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.R;

public class MailReadFragment extends Fragment implements MailReadContract.View {

    private TextView mTitleTV;
    private TextView mMessageTV;
    private TextView mSenderTV;
    private TextView mReceptionLocation;
    private MailReadContract.Presenter mPresenter;

    private Mail mUserMail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mail_read_frag, container, false);

        mTitleTV = (TextView) root.findViewById(R.id.read_title);
        // set the contents
        mMessageTV = (TextView) root.findViewById(R.id.read_message);
        mSenderTV = (TextView) root.findViewById(R.id.read_sender);

        mReceptionLocation = (TextView) root.findViewById(R.id.reception_location);
        mReceptionLocation.setOnLongClickListener(getReceptionLocationFieldLongClickListener());

        presentMail();

        return root;
    }

    private void presentMail() {
        mTitleTV.setText(mUserMail.getTitle());
        mMessageTV.setText(mUserMail.getSender());
        mSenderTV.setText(mUserMail.getSender());
    }

    @Override
    public void setPresenter(MailReadContract.Presenter presenter) {
        mPresenter = presenter;
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

    private View.OnLongClickListener getReceptionLocationFieldLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPresenter.receptionLocationLongClicked();
                return false;
            }
        };
    }

    public static MailReadFragment newInstance() {
        return new MailReadFragment();
    }

    @Override
    public void setMail(Mail mail) {
        mUserMail = mail;
    }
}
