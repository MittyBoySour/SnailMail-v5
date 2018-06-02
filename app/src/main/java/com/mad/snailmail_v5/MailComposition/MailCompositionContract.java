package com.mad.snailmail_v5.MailComposition;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.User;

public interface MailCompositionContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

        void submitMailButtonClicked(String title, String recipient, String message);

        void discardMailButtonClicked();

        void DeliveryLocationLongClicked(boolean displayingMap);

    }

    interface View extends BaseView<Presenter> {

        void showMiddleView(int viewOption);
    }
}
