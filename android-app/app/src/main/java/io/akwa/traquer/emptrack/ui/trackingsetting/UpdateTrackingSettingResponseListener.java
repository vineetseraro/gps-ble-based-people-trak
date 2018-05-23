package io.akwa.traquer.emptrack.ui.trackingsetting;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.trakit.rulehandler.GetTrackingSettingResponse;

/**
 * Created by rohitkumar on 11/1/17.
 */

public interface UpdateTrackingSettingResponseListener {
    void onUpdateSettingsResponseReceive(GetTrackingSettingResponse response, NicbitException e);

}
