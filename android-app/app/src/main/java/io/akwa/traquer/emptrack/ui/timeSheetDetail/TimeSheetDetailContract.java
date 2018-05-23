package io.akwa.traquer.emptrack.ui.timeSheetDetail;



import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.ui.timeSheetDetail.model.TimeSheetDetailResponse;

public class TimeSheetDetailContract {
   public interface View {

        void onTimeSheetDetailDone(TimeSheetDetailResponse response, NicbitException e);
   }

    interface UserActionsListener {
        void getTimeSheetDetail(String date);

    }
}
