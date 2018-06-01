package com.mad.snailmail_v5.MailList;

import java.util.ArrayList;
import java.util.List;

import BaseInterfaces.BasePresenter;
import BaseInterfaces.BaseView;
import Model.Mail;

public interface MailListContract {

    interface Presenter extends BasePresenter {

        void updateMailList();

        void addNewUser(String username);
    }

    interface View extends BaseView<Presenter> {

        void displayMailListFromAdapter();

        void newUserRegistered(String username);

    }
}
