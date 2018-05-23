package io.akwa.traquer.emptrack.ui.settings;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.listener.SettingsResponseListener;
import io.akwa.traquer.emptrack.listener.UpdateSettingsResponseListener;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.model.UpdateSettingsRequest;

public class SettingPresenter implements SettingContract.UserActionsListener, UpdateSettingsResponseListener, SettingsResponseListener {
    private final SettingContract.View mSettingView;

    public SettingPresenter(SettingContract.View mSettingView) {
        this.mSettingView = mSettingView;
    }






    @Override
    public void getSettings() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setSettingsResponseListener(this);
        apiHandler.getSettings();

    }

    @Override
    public void updateSettings(UpdateSettingsRequest updateSettingsRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateSettingsResponseListener(this);
        apiHandler.updateSettings(updateSettingsRequest);
    }

    @Override
    public void onUpdateSettingsResponseReceive(ApiResponseModel response, NicbitException e) {
        mSettingView.onUpdateSettingsResponseReceive(response, e);

    }

    @Override
    public void onSettingsResponseReceive(ApiResponseModel response, NicbitException e) {
        mSettingView.onGetSettingsResponseReceive(response, e);
    }
}
