package com.mad.snailmail_v5.MailRead;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.snailmail_v5.R;

public class MailReadFragment extends Fragment implements MailReadContract.View {

    private TextView mTitleTV;
    private TextView mMessageTV;
    private TextView mSenderTV;
    private TextView mReceptionLocation;
    private MailReadContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mail_read_frag, container, false);

        mTitleTV = (TextView) root.findViewById(R.id.composition_title);
        // set the contents
        mMessageTV = (TextView) root.findViewById(R.id.composition_message);
        mSenderTV = (TextView) root.findViewById(R.id.composition_recipient);

        mReceptionLocation = (TextView) root.findViewById(R.id.delivery_address);
        mReceptionLocation.setOnLongClickListener(getReceptionLocationFieldLongClickListener());

        return root;
    }

    @Override
    public void setPresenter(MailReadContract.Presenter presenter) {
        mPresenter = presenter;
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
}
