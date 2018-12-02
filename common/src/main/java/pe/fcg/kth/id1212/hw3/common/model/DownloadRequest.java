package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class DownloadRequest implements Serializable {
    private SessionContext sessionContext;
    private String fileName;

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
