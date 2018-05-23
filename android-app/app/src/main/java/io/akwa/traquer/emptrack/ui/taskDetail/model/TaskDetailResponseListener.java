package io.akwa.traquer.emptrack.ui.taskDetail.model;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.timeSheetDetail.model.TimeSheetDetailResponse;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface TaskDetailResponseListener {
    void onTaskDetailResponseReceive(TaskDetailResponse response, NicbitException e);
}
