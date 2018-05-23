package io.akwa.traquer.emptrack.common.updatedevice;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public class UpdateDevicePresneter {


   public interface View {
        void onDeviceUpdate(ApiResponseModel response, NicbitException e);
    }

    public interface UserActionsListener {
        void deviceUpdate(DeviceInfo deviceInfor);
    }
}
