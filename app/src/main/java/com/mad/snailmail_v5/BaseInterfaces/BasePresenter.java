package com.mad.snailmail_v5.BaseInterfaces;

import com.mad.snailmail_v5.Model.BareGeofence;
import com.mad.snailmail_v5.Model.Mail;
import com.mad.snailmail_v5.Model.User;

import java.util.ArrayList;

public interface BasePresenter {

    void start();

    void userMailListResponse(ArrayList<Mail> mailList);

    void updateUserContactList(ArrayList<String> UserContactList);

    void permissionsNotSet(boolean firstRequest, String[] permissions);

    void setCurrentUser(User user);

    void bareGeofenceResponse(ArrayList<BareGeofence> bareGeofenceList);

    void userListResponse();

    void userExistenceResponse(boolean userExists);

    void userSuccessfullyAdded();

}
