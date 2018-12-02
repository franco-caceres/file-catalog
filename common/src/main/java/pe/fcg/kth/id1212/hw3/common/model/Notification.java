package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class Notification implements Serializable {
    public enum Action {
        READ, UPDATE, DELETE
    }

    private Action action;
    private String filename;
    private String username;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
