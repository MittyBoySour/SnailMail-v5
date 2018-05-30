package com.mad.snailmail_v5.MailList;

import java.util.List;

import BaseInterfaces.BasePresenter;
import BaseInterfaces.BaseView;
import Model.Mail;

public interface MailListContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

        void showMail(List<Mail> mailList);

    }
}
