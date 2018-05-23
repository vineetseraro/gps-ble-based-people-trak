package io.akwa.traquer.emptrack.listener;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public interface SettingsResponseListener {
    void onSettingsResponseReceive(ApiResponseModel response, NicbitException e);

}
