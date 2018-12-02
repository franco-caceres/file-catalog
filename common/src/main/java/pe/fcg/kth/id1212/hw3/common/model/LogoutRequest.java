package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class LogoutRequest implements Serializable {
    private SessionContext sessionContext;

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
