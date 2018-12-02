package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class SignUpResponse implements Serializable {
    public enum Code {
        OK, USERNAME_EXISTS
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
