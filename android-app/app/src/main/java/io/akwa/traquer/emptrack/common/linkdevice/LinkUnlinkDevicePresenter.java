package io.akwa.traquer.emptrack.common.linkdevice;

import com.google.gson.JsonObject;

import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;


public class LinkUnlinkDevicePresenter implements LinkUnlinkDeviceContract.UserActionsListener,LinkUnlinkDeviceListener{

    private final LinkUnlinkDeviceContract.View mHomeView;

    public LinkUnlinkDevicePresenter(LinkUnlinkDeviceContract.View mHomeView) {
        this.mHomeView = mHomeView;
    }


    @Override
    public void linkDevice(JsonObject code) {
         ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setLinkUnlinkDeviceListener(this);
        apiHandler.linkDevice(code);
    }

    @Override
    public void unLinkDevice(JsonObject code) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setLinkUnlinkDeviceListener(this);
        apiHandler.unLinkDevice(code);
    }


    @Override
    public void onDeviceLink(ApiResponseModel response, NicbitException e) {
        mHomeView.onLinkDevice(response, e);
    }

    @Override
    public void onDeviceUnlink(ApiResponseModel response, NicbitException e) {
        mHomeView.onUnLinkDevice(response, e);
    }
}
