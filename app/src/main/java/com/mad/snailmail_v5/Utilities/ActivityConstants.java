package com.mad.snailmail_v5.Utilities;

public class ActivityConstants {

    public class ActivityKeys {

        ///////////// SHARED PREFERENCE KEYS /////////////

        public static final String DEFAULT_SHARED_PREFERENCES_KEY = "default_shared_preferences_key";

        ///////////// CONSTANTS AND KEYS ///////////////////

        public static final String CURRENT_USER_KEY = "current_user_key";
        public static final String MAIL_ITEM_KEY = "mail_item_key";

//        public static final String BARE_GEOFENCE_LATITUDE_STORAGE_KEY = "bareGeofenceLatitude";
//        public static final String BARE_GEOFENCE_LONGITUDE_STORAGE_KEY = "bareGeofenceLongitude";
//        public static final String BARE_GEOFENCE_COLLECTED_STATUS_STORAGE_KEY = "bareGeofenceCollectedStatus";

        public static final String PENDING_GEOFENCE_TRANSITION_KEY = "pending_geofence_transition_key";
        public static final String PENDING_GEOFENCE_INTENT_KEY = "pending_geofence_intent_key";
        public static final int PENDING_GEOFENCE_INTENT_BUNDLE = 5001;

        public static final String SELECTED_LOCATION_KEY = "selected_location_key";
        public static final String LOCATION_LATITUDE_KEY = "location_latitude_key";
        public static final String LOCATION_LONGITUDE_KEY = "location_longitude_key";
        public static final String LOCATION_TITLE_KEY = "location_title_key";
        public static final String MARKER_TITLE = "marker_title";
        public static final String MAIL_COORDINATES = "mail_coordinates";
        public static final float GEOFENCE_RADIUS_IN_METRES = 100; /* suggested minimum */
        // public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 2628000;

        ///////////// PERMISSIONS /////////////////

        public static final int MAIL_COMPOSITION_PERMISSIONS_REQUEST = 2001;

        ///////////// REQUEST CODES //////////////////

        public static final int LOCATOR_ACTIVITY_REQUEST_CODE = 3001;
        public static final int GEOFENCE_PENDING_INTENT_REQUEST = 4001;

        ///////////// HASH KEYS ///////////////////

        public final String IS_COLLECTED_HASH_KEY = "isCollected";
        public final String LATITUDE_HASH_KEY = "latitude";
        public final String LONGITUDE_HASH_KEY = "longitude";


    }

    public class LocationConstants {

        public static final double SYDNEY_LATITUDE = -33.852;
        public static final double SYDNEY_LONGITUDE = 151.211;
        public static final double MELBOURNE_LATITUDE = -37.815;
        public static final double MELBOURNE_LONGITUDE = -37.815;

    }

}
