package io.akwa.traquer.emptrack.updatedevice;


import io.akwa.traquer.emptrack.common.updatedevice.DeviceInfo;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiBaseResponse;

public class RegisterUpdateDevicePresneter {


   public interface View {
        void onDeviceUpdate(ApiBaseResponse response, NicbitException e);
    }

    public interface UserActionsListener {
        void deviceUpdate(DeviceInfo deviceInfor);
    }
}
