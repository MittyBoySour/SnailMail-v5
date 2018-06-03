package com.mad.snailmail_v5.Roaming;

import android.os.Bundle;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;

import java.util.ArrayList;

public interface RoamingContract {

    interface Presenter extends BasePresenter {

        void geofencesRequested();

        void IntentResultBundleReceived(Bundle resultData);

        void mapClicked(LatLng latLng);

    }

    interface View extends BaseView<Presenter> {

        RoamingActivity.IntentResultHandler returnResultHandler();

        void setTextViewCoordinates(double latitude, double longitude);

        void setGeofenceCirclesOnMap(ArrayList<CircleOptions> circleOptionsList);

    }
}
