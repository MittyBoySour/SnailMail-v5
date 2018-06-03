package com.mad.snailmail_v5.Utilities;

import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.Model.User;

public interface BaseFirebaseManager<P extends BasePresenter> {

    BaseFirebaseManager getInstance();

    void setPresenter(P presenter);

    void setUser(User user);

    User getCurrentUser();

}
