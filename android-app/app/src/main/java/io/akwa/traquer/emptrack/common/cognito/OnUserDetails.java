package io.akwa.traquer.emptrack.common.cognito;


import io.akwa.traquer.emptrack.model.ReaderGetProfileResponse;

/**
 * Created by rohitkumar on 7/6/17.
 */

public interface OnUserDetails {
    public void onUserDetails(ReaderGetProfileResponse data, Exception exception);

}
