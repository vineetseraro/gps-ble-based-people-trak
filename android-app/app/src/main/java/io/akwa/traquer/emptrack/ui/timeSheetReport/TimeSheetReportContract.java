package io.akwa.traquer.emptrack.ui.timeSheetReport;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.timeSheetReport.model.TimeSheetReportResponse;

public class TimeSheetReportContract {
   public interface View {

        void onTimeSheetReportDone(TimeSheetReportResponse response, NicbitException e);
   }

    interface UserActionsListener {
        void getTimeSheetReport(String fromDate,String toDate);

    }
}
