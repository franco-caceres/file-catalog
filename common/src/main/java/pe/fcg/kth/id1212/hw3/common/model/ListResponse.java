package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;
import java.util.List;

public class ListResponse implements Serializable {
    public enum Code {
        OK, NO_SESSION
    }

    private Code code = Code.OK;
    private List<File> files;

    public boolean isSuccessful() {
        return Code.OK.equals(code);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
