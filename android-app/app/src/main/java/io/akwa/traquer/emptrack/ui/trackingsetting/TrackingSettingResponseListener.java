package io.akwa.traquer.emptrack.ui.trackingsetting;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.trakit.rulehandler.GetTrackingSettingResponse;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface TrackingSettingResponseListener {
    void onSettingsResponseReceive(GetTrackingSettingResponse response, NicbitException e);
}
