package io.akwa.traquer.emptrack.common.updatedevice;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public interface DeviceInformationResponseListener {

    void onDeviceUpdate(ApiResponseModel response, NicbitException e);
}
