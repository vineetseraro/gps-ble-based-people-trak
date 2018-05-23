package io.akwa.traquer.emptrack.common.linkdevice;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

/**
 * Created by rohitkumar on 9/14/17.
 */

public interface LinkUnlinkDeviceListener {

    void onDeviceLink(ApiResponseModel response, NicbitException e);
    void onDeviceUnlink(ApiResponseModel response, NicbitException e);
}
