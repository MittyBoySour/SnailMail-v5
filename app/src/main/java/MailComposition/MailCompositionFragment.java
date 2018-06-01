package MailComposition;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mad.snailmail_v5.R;

public class MailCompositionFragment extends Fragment implements MailCompositionContract.View {

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

        return root;
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    public MailCompositionFragment newInstance() {
        return new MailCompositionFragment();
    }
}
