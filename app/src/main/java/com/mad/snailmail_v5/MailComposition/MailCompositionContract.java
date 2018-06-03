package com.mad.snailmail_v5.MailComposition;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.mad.snailmail_v5.BaseInterfaces.BasePresenter;
import com.mad.snailmail_v5.BaseInterfaces.BaseView;
import com.mad.snailmail_v5.Model.User;

public interface MailCompositionContract {

    interface Presenter extends BasePresenter {

        void submitMailButtonClicked(String title, String recipient, String message);

        void discardMailButtonClicked(String[] textFields);

        void deliveryLocationLongClicked();

        void setLocationClient(FusedLocationProviderClient mFusedLocationClient);

        void requestLocation();

        void deliveryLocationClicked();

    }

    interface View extends BaseView<Presenter> {

        void updateDeliveryLocation(String markerTitle);

    }
}
