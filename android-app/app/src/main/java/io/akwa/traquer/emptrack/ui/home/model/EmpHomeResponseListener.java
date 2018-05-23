package io.akwa.traquer.emptrack.ui.home.model;

import io.akwa.traquer.emptrack.exception.NicbitException;

/**
 * Created by rohitkumar on 10/31/17.
 */

public interface EmpHomeResponseListener {
    void onDashboardResponseReceive(EmpDashboardResponse response, NicbitException e);
}
