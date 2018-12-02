package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class UploadResponse implements Serializable {
    public enum Code {
        OK, NO_SESSION, FORBIDDEN
    }

    private Code code = Code.OK;
    private File file;

    boolean isSuccessful() {
        return Code.OK.equals(code);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public File getFileDto() {
        return file;
    }

    public void setFileDto(File fileDto) {
        this.file = fileDto;
    }
}
