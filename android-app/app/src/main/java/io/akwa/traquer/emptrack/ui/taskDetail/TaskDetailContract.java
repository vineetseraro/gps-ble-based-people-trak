package io.akwa.traquer.emptrack.ui.taskDetail;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailResponse;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailUpdateRequest;

public class TaskDetailContract {
    public interface View {

        void onTaskDetailDone(TaskDetailResponse response, NicbitException e);
        void onTaskDetailUpdated(ApiResponseModel response, NicbitException e);
    }

    interface UserActionsListener {
        void getTaskDetail(String taskId);
        void updateTaskDetail(String taskId,TaskDetailUpdateRequest taskDetailUpdateRequest);

    }
}
