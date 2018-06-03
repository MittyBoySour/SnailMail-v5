package com.mad.snailmail_v5.BaseInterfaces;

import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public interface BaseView<P> {

    void setPresenter(P presenter);

    void displayDialogue(AlertDialog dialogue);

    void requestPermissions(String[] permissions);

    void displayToast(Toast toast);

}
