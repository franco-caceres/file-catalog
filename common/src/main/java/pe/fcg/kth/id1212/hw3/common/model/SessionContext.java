package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SessionContext implements Serializable {
    private UUID key;

    public SessionContext() {
        key = UUID.randomUUID();
    }

    public UUID getKey() {
        return key;
    }

    public void setKey(UUID key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionContext that = (SessionContext) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key);
    }
}
