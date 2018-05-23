package io.akwa.traquer.emptrack.ui.home;



import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.ui.home.model.EmpDashboardResponse;

public class EmpHomeContract {
   public interface View {
        void onLogoutDone(ApiResponseModel loginResponse, NicbitException e);
        void onDashboardDone(EmpDashboardResponse response, NicbitException e);
   }

    interface UserActionsListener {
        void getDashboard(String date);
        void doLogout();
    }
}
