package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class UploadRequest implements Serializable {
    private SessionContext sessionContext;
    private File file;

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
