package com.mad.snailmail_v5.MailComposition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mad.snailmail_v5.R;

public class MailCompositionFragment extends Fragment implements MailCompositionContract.View {

    private MailCompositionContract.Presenter mPresenter;

    private Button mSubmitMailButton;
    private Button mDiscardMailButton;
    private EditText mTitleTV;
    private EditText mRecipientTV;
    private TextView mDeliveryLocationTV; // store in other activity, and don't allow click until selected
    private EditText mMessageTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // assign to the xml file

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // could potentially move this all to onCreate if no other frag/state will exist for activity

        View root = inflater.inflate(R.layout.mail_composition_frag, container, false);

        mSubmitMailButton = (Button) root.findViewById(R.id.send_composition);
        mSubmitMailButton.setOnClickListener(getSubmitMailButtonListener());
        mDiscardMailButton = (Button) root.findViewById(R.id.discard_composition);
        mDiscardMailButton.setOnClickListener(getDiscardMailButtonListener());

        mTitleTV = (EditText) root.findViewById(R.id.composition_title);
        mRecipientTV = (EditText) root.findViewById(R.id.composition_recipient);

        mDeliveryLocationTV = (TextView) root.findViewById(R.id.delivery_address);
        mDeliveryLocationTV.setOnLongClickListener(getDeliveryLocationFieldLongClickListener());
        checkDeliveryLocation();

        mMessageTV = (EditText) root.findViewById(R.id.composition_message);

        return root;
    }

    @Override
    public void setPresenter(MailCompositionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static MailCompositionFragment newInstance() {
        return new MailCompositionFragment();
    }

    public View.OnClickListener getSubmitMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleTV.getText().toString();
                String recipient = mRecipientTV.getText().toString();
                // LatLng deliveryLocation;
                String message = mMessageTV.getText().toString();
                mPresenter.submitMailButtonClicked(title, recipient, message);
            }
        };
    }

    private View.OnClickListener getDiscardMailButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.discardMailButtonClicked();
            }
        };
    }

    public View.OnLongClickListener getDeliveryLocationFieldLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // start locator activity
                // possibly make into fragment rather than activity and bundle data
                return false;
            }
        };
    }

    private void checkDeliveryLocation() {
        // check the passed data to see if location has been selected

    }

}
