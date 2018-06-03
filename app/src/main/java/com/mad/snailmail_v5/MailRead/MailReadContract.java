package com.mad.snailmail_v5.MailRead;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.Mail;

public interface MailReadContract {

    interface Presenter extends BasePresenter {

        void receptionLocationLongClicked();

        void replyFABClicked();

    }

    interface View extends BaseView<Presenter> {

        void setMail(Mail mail);
    }

}
