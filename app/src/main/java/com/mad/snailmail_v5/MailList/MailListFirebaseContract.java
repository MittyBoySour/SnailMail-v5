package com.mad.snailmail_v5.MailList;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.Utilities.BaseFirebaseManager;

public interface MailListFirebaseContract {

    interface Presenter extends BasePresenter {

    }

    interface FirebaseManager extends BaseFirebaseManager<Presenter> {

    }

}
