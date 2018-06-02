package com.mad.snailmail_v5.MailList;

import android.support.v7.widget.RecyclerView;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

public interface MailListContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

        void mailListRefreshRequested();

        void updateMailAdapter(MailAdapter mailAdapter);

        void addNewUser(String username);

    }

    interface View extends BaseView<Presenter> {

        void attachMailAdapter(MailAdapter mailAdapter);

        // maybe add functionality to lookup users after a 4 or more chars entered



    }
}
