package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;
import java.util.Objects;

public class LoginRequest implements Serializable {
    private String username;
    private String password;
    private NotificationHandler notificationHandler;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(notificationHandler, that.notificationHandler);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, password, notificationHandler);
    }
}
