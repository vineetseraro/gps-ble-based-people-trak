package io.akwa.traquer.emptrack.ui.timeSheetReport;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.timeSheetReport.model.TimeSheetReportResponse;
import io.akwa.traquer.emptrack.ui.timeSheetReport.model.TimeSheetReportResponseListener;

public class TimeSheetReportPresenter implements TimeSheetReportContract.UserActionsListener,TimeSheetReportResponseListener {

    private final TimeSheetReportContract.View mView;

    public TimeSheetReportPresenter(TimeSheetReportContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onTimeSheetReportResponseReceive(TimeSheetReportResponse response, NicbitException e) {
        mView.onTimeSheetReportDone(response,e);
    }

    @Override
    public void getTimeSheetReport(String fromDate,String toDate) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setTimeSheetReportResponseListener(this);
        apiHandler.getTimeSheetReport(fromDate,toDate);
    }
}
