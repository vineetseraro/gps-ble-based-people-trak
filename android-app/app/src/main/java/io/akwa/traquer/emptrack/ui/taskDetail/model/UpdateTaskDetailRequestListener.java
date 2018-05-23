package io.akwa.traquer.emptrack.ui.taskDetail.model;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface UpdateTaskDetailRequestListener {
    void onTaskDetailUpdated(ApiResponseModel response, NicbitException e);
}
