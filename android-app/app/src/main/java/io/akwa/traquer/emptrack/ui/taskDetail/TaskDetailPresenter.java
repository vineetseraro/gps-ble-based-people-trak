package io.akwa.traquer.emptrack.ui.taskDetail;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.ApiResponseModel;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailResponse;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailResponseListener;
import io.akwa.traquer.emptrack.ui.taskDetail.model.TaskDetailUpdateRequest;
import io.akwa.traquer.emptrack.ui.taskDetail.model.UpdateTaskDetailRequestListener;

public class TaskDetailPresenter implements TaskDetailContract.UserActionsListener,TaskDetailResponseListener, UpdateTaskDetailRequestListener {

    private final TaskDetailContract.View mView;

    public TaskDetailPresenter(TaskDetailContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onTaskDetailResponseReceive(TaskDetailResponse response, NicbitException e) {
        mView.onTaskDetailDone(response,e);
    }

    @Override
    public void getTaskDetail(String taskId) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setTaskDetailResponseListener(this);
        apiHandler.getTaskDetail(taskId);
    }

    @Override
    public void updateTaskDetail(String taskId, TaskDetailUpdateRequest taskDetailUpdateRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setUpdateTaskDetailRequestListener(this);
        apiHandler.updateTaskDetail(taskId,taskDetailUpdateRequest);
    }

    @Override
    public void onTaskDetailUpdated(ApiResponseModel response, NicbitException e) {
        mView.onTaskDetailUpdated(response,e);
    }
}
