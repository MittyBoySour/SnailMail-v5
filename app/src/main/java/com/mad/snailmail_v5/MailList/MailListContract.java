package com.mad.snailmail_v5.MailList;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import BaseInterfaces.BasePresenter;
import BaseInterfaces.BaseView;
import Model.Mail;
import Model.User;

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
