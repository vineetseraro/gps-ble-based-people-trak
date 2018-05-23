package io.akwa.traquer.emptrack.listener;


import io.akwa.traquer.emptrack.exception.NicbitException;
import io.akwa.traquer.emptrack.model.NotificationApiResponse;

public interface NotificationListListener {
    void onNotificationListReceive(NotificationApiResponse response, NicbitException e);
    void onNotificationRemove(NotificationApiResponse response, NicbitException e);
}
