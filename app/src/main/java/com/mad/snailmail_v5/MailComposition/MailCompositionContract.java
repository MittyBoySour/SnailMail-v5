package com.mad.snailmail_v5.MailComposition;

import BaseInterfaces.BasePresenter;
import BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.User;

public interface MailCompositionContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

        void submitMailButtonClicked(String title, String recipient, String message);
    }

    interface View extends BaseView<Presenter> {

    }
}
