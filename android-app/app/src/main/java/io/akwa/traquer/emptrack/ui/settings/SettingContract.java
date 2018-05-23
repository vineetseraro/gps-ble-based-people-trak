package io.akwa.traquer.emptrack.ui.settings;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.model.UpdateSettingsRequest;

public interface SettingContract {

    interface View {
        void onGetSettingsResponseReceive(ApiResponseModel response, NicbitException e);
        void onUpdateSettingsResponseReceive(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {

        void getSettings();
        void updateSettings(UpdateSettingsRequest updateSettingsRequest);
    }
}
