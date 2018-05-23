package io.akwa.traquer.emptrack.ui.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.akwa.traquer.emptrack.model.BaseResponse;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailItem;

/**
 * Created by rohitkumar on 10/31/17.
 */

public class EmpDashboardResponse extends BaseResponse{


    @SerializedName("data")
    @Expose

    DashBoardData data;

    public DashBoardData getData() {
        return data;
    }

    public void setData(DashBoardData data) {
        this.data = data;
    }

    public class DashBoardData {

        long totalIn;
        ArrayList<TaskDetailItem> tasks;

        public ArrayList<TaskDetailItem> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<TaskDetailItem> tasks) {
            this.tasks = tasks;
        }

        public long getTotalIn() {
            return totalIn;
        }

        public void setTotalIn(long totalIn) {
            this.totalIn = totalIn;
        }
    }

}
