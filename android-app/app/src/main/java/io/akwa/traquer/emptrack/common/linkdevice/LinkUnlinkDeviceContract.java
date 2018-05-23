package io.akwa.traquer.emptrack.common.linkdevice;

import com.google.gson.JsonObject;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public interface LinkUnlinkDeviceContract {

    interface View {
        void onUnLinkDevice(ApiResponseModel loginResponse, NicbitException e);
        void onLinkDevice(ApiResponseModel loginResponse, NicbitException e);
    }

    interface UserActionsListener {
        void linkDevice(JsonObject code);
        void unLinkDevice(JsonObject code);
    }
}
