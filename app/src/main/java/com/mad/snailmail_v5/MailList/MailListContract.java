package com.mad.snailmail_v5.MailList;

import android.support.v7.widget.RecyclerView;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

public interface MailListContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

        void updateMailList();

        void addNewUser(String username);

        void addContactForUser(String contactUsername);

        RecyclerView.Adapter getUserMailAdapter();

        void sendMailToContact(Mail mail);
    }

    interface View extends BaseView<Presenter> {

        void displayMailListFromAdapter();

        void registerNewUserButtonClicked(String username);

        void addNewContactButtonClicked(String contactUsername);

        // maybe add functionality to lookup users after a 4 or more chars entered



    }
}
