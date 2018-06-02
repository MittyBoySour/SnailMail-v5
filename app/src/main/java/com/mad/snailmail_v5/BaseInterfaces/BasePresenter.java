package com.mad.snailmail_v5.BaseInterfaces;

import com.mad.snailmail_v5.MailList.MailAdapter;

public interface BasePresenter {

    void start();

    void updateMailAdapter(MailAdapter mailAdapter);

    void updateContactList();

}
