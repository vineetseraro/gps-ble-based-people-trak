package io.akwa.traquer.emptrack.common.updatedevice;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public class DeviceApiHandler implements  UpdateDevicePresneter.UserActionsListener,DeviceInformationResponseListener{
    private final UpdateDevicePresneter.View mCaseView;

    public DeviceApiHandler(UpdateDevicePresneter.View mCaseView) {
        this.mCaseView = mCaseView;
    }


    @Override
    public void deviceUpdate(DeviceInfo deviceInfor) {

     /*   ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setDeviceInformationResponseListener(this);
        apiHandler.updateDeviceInformation(deviceInfor);
*/
    }

    @Override
    public void onDeviceUpdate(ApiResponseModel response, NicbitException e) {

        mCaseView.onDeviceUpdate(response,e);

    }
}

