package io.akwa.traquer.emptrack.ui.home;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.listener.LogoutResponseListener;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.ui.home.model.EmpDashboardResponse;
import io.akwa.traquer.emptrack.ui.home.model.EmpHomeResponseListener;

public class DashboardHomePresenter implements EmpHomeContract.UserActionsListener,LogoutResponseListener,EmpHomeResponseListener {

    private final EmpHomeContract.View mView;

    public DashboardHomePresenter(EmpHomeContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getDashboard(String date) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setEmpHomeResponseListener(this);
        apiHandler.getDashboardData(date);
    }

    @Override
    public void onLogoutResponse(ApiResponseModel response, NicbitException e) {
        mView.onLogoutDone(response, e);
    }

    @Override
    public void doLogout() {
//        ApiHandler apiHandler = ApiHandler.getApiHandler();
//        apiHandler.setLogoutResponseListener(this);
//        apiHandler.logout();
//        AppHelper.getPool().getCurrentUser().signOut();
        mView.onLogoutDone(null, null);

    }

    @Override
    public void onDashboardResponseReceive(EmpDashboardResponse response, NicbitException e) {
        mView.onDashboardDone(response,e);
    }
}
