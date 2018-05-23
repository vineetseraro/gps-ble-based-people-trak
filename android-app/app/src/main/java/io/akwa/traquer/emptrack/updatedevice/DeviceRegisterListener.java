package io.akwa.traquer.emptrack.updatedevice;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiBaseResponse;

/**
 * Created by rohitkumar on 7/12/17.
 */

public interface DeviceRegisterListener {

    void onDeviceUpdate(ApiBaseResponse response, NicbitException e);

}
