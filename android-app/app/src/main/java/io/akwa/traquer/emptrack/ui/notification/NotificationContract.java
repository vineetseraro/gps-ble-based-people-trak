package io.akwa.traquer.emptrack.ui.notification;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.NotificationApiResponse;
import io.akwa.traquer.emptrack.model.RemoveNotificationRequest;

public interface NotificationContract {

    interface View {
        void onNotificationReceive(NotificationApiResponse response, NicbitException e);
        void onNotificationRemoved(NotificationApiResponse response, NicbitException e);
    }

    interface UserActionsListener {
        void removeNotifications(RemoveNotificationRequest removeNotificationRequest);
        void getNotificationList();
    }
}
