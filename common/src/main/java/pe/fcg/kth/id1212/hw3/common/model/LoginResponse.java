package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public enum Code {
        OK, USERNAME_NOT_FOUND, INVALID_CREDENTIALS
    }

    private Code code = Code.OK;
    private SessionContext sessionContext;
    private int serverPort;

    public boolean isSuccessful() {
        return Code.OK.equals(code);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
