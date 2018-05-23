package io.akwa.traquer.emptrack.ui.contactsettings;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.contactsettings.model.ContactSettingRequest;
import io.akwa.traquer.emptrack.ui.contactsettings.model.GetContactSettingsResponse;

public interface ContactSettingContract {

    interface View {
        void onGetSettingsResponseReceive(GetContactSettingsResponse response, NicbitException e);
        void onUpdateSettingsResponseReceive(GetContactSettingsResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void getSettings();
        void updateSettings(ContactSettingRequest updateSettingsRequest);
    }
}
