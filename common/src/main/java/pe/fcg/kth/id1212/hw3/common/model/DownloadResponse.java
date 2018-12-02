package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class DownloadResponse implements Serializable {
    public enum Code {
        OK, NO_SESSION, NOT_FOUND
    }

    private Code code = Code.OK;
    private File file;

    public boolean isSuccessful() {
        return Code.OK.equals(code);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
