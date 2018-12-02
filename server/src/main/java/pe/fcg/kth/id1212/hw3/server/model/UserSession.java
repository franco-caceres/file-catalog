package pe.fcg.kth.id1212.hw3.server.model;

import pe.fcg.kth.id1212.hw3.common.model.NotificationHandler;

public class UserSession {
    private String username;
    private NotificationHandler notificationHandler;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }
}
