package com.mad.snailmail_v5.MailList;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

import java.util.ArrayList;

public interface MailListContract {

    interface Presenter extends BasePresenter {

        void mailListRefreshRequested();

        void userMailListResponse(ArrayList<Mail> mailList);

        void addNewUser(String username);

        void mailTitleClicked(Mail mail);

    }

    interface View extends BaseView<Presenter> {

        void attachMailAdapter(MailAdapter mailAdapter);

        // maybe add functionality to lookup users after a 4 or more chars entered

    }
}
