package io.akwa.traquer.emptrack.ui.taskDetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.akwa.traquer.emptrack.model.BaseResponse;
import io.akwa.traquer.emptrack.ui.timeSheetDetail.model.TimeSheetDetailItem;

/**
 * Created by rohitkumar on 10/31/17.
 */

public class TaskDetailResponse extends BaseResponse{


    @SerializedName("data")
    @Expose

    TaskDetailItem data;

    public TaskDetailItem getData() {
        return data;
    }

    public void setData(TaskDetailItem data) {
        this.data = data;
    }
}
