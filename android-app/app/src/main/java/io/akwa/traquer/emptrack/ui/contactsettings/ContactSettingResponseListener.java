package io.akwa.traquer.emptrack.ui.contactsettings;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.contactsettings.model.GetContactSettingsResponse;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface ContactSettingResponseListener {
    void onSettingsResponseReceive(GetContactSettingsResponse response, NicbitException e);
}
