package io.akwa.traquer.emptrack.ui.timeSheetDetail.model;

import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.home.model.EmpDashboardResponse;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface TimeSheetDetailResponseListener {
    void onTimeSheetDetailResponseReceive(TimeSheetDetailResponse response, NicbitException e);
}
