package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class DeleteResponse implements Serializable {
    public enum Code {
        OK, NO_SESSION, NOT_FOUND, FORBIDDEN
    }

    private Code code = Code.OK;

    public boolean isSuccessful() {
        return Code.OK.equals(code);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}
