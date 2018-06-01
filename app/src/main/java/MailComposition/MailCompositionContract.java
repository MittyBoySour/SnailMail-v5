package MailComposition;

import BaseInterfaces.BasePresenter;
import BaseInterfaces.BaseView;
import Model.User;

public interface MailCompositionContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

    }

    interface View extends BaseView {

    }
}
