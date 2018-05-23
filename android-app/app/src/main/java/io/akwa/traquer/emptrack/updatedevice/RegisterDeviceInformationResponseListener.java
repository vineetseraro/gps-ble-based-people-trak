package io.akwa.traquer.emptrack.updatedevice;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiBaseResponse;

public interface RegisterDeviceInformationResponseListener {

    void onDeviceUpdate(ApiBaseResponse response, NicbitException e);
}
