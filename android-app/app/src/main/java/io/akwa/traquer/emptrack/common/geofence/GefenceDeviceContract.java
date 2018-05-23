package io.akwa.traquer.emptrack.common.geofence;

import io.akwa.traquer.emptrack.exception.NicbitException;

public interface GefenceDeviceContract {

    interface View {
        void onGeofencesReceived(GeofenceApiResponse loginResponse, NicbitException e);
    }

    interface UserActionsListener {
        void getGeofences();
    }
}
