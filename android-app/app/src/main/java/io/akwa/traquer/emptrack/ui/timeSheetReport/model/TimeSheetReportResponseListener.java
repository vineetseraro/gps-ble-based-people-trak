package io.akwa.traquer.emptrack.ui.timeSheetReport.model;

import io.akwa.traquer.emptrack.exception.NicbitException;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface TimeSheetReportResponseListener {
    void onTimeSheetReportResponseReceive(TimeSheetReportResponse response, NicbitException e);
}
