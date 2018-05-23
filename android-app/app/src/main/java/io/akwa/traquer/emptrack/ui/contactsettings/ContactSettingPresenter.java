package io.akwa.traquer.emptrack.ui.contactsettings;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.contactsettings.model.ContactSettingRequest;
import io.akwa.traquer.emptrack.ui.contactsettings.model.GetContactSettingsResponse;


public class ContactSettingPresenter implements ContactSettingContract.UserActionsListener, UpdateContactSettingResponse, ContactSettingResponseListener {
    private final ContactSettingContract.View mSettingView;

    public ContactSettingPresenter(ContactSettingContract.View mSettingView) {
        this.mSettingView = mSettingView;
    }



    @Override
    public void getSettings() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setContactSettingResponseListener(this);
        apiHandler.getContactSettings();

    }

    @Override
    public void updateSettings(ContactSettingRequest updateSettingsRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateContactSettingResponse(this);
        apiHandler.updateContactSetting(updateSettingsRequest);
    }

    @Override
    public void onUpdateContactSetting(GetContactSettingsResponse response, NicbitException e) {
        mSettingView.onUpdateSettingsResponseReceive(response, e);

    }

    @Override
    public void onSettingsResponseReceive(GetContactSettingsResponse response, NicbitException e) {
        mSettingView.onGetSettingsResponseReceive(response, e);
    }


}
