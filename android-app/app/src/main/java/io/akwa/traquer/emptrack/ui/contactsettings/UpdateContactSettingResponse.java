package io.akwa.traquer.emptrack.ui.contactsettings;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.contactsettings.model.GetContactSettingsResponse;

/**
 * Created by rohitkumar on 11/1/17.
 */

public interface UpdateContactSettingResponse {
    void onUpdateContactSetting(GetContactSettingsResponse response, NicbitException e);

}
