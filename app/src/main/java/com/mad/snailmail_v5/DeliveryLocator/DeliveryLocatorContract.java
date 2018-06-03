package com.mad.snailmail_v5.DeliveryLocator;

import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.User;

public interface DeliveryLocatorContract {

    interface Presenter extends BasePresenter {

        void setCurrentUser(User user);

        void mapLongClicked(LatLng latLng);

        void markerClicked(Marker marker);

        void activityFinished();

    }

    interface View extends BaseView<Presenter> {

        void addMarkerFromOptions(MarkerOptions options);

        void moveCamera(CameraUpdate cameraUpdate);

        void displayDialogue(AlertDialog dialogue);
    }

}
