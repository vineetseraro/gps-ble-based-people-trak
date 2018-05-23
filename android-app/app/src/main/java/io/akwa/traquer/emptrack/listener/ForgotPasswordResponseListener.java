package io.akwa.traquer.emptrack.listener;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

public interface ForgotPasswordResponseListener {
    void onForgotPasswordResponse(ApiResponseModel response, NicbitException e);
}
