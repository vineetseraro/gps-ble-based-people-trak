package io.akwa.traquer.emptrack.ui.notification;


import io.akwa.traquer.emptrack.common.network.ApiHandler;
import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.listener.NotificationListListener;
import io.akwa.traquer.emptrack.model.NotificationApiResponse;
import io.akwa.traquer.emptrack.model.RemoveNotificationRequest;

public class NotificationPresenter implements NotificationContract.UserActionsListener,NotificationListListener {
    private final NotificationContract.View mNotificationView;

    public NotificationPresenter(NotificationContract.View mNotificationView) {
        this.mNotificationView = mNotificationView;
    }

    @Override
    public void removeNotifications(RemoveNotificationRequest removeNotificationRequest) {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setNotificationListListener(this);
        apiHandler.removeNotification(removeNotificationRequest);
    }

    @Override
    public void getNotificationList() {
        ApiHandler apiHandler = ApiHandler.getApiHandler();
        apiHandler.setNotificationListListener(this);
        apiHandler.getNotifications();
    }

    @Override
    public void onNotificationListReceive(NotificationApiResponse response, NicbitException e) {
        mNotificationView.onNotificationReceive(response, e);
    }

    @Override
    public void onNotificationRemove(NotificationApiResponse response, NicbitException e) {
        mNotificationView.onNotificationRemoved(response, e);

    }


}
