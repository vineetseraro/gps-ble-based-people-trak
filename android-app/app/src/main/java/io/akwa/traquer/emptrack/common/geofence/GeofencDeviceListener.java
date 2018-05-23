package io.akwa.traquer.emptrack.common.geofence;


import io.akwa.traquer.emptrack.exception.NicbitException;

/**
 * Created by rohitkumar on 9/14/17.
 */

public interface GeofencDeviceListener {

    void onGeofenceRecived(GeofenceApiResponse response, NicbitException e);

}
