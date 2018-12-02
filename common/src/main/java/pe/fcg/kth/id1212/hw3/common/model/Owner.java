package pe.fcg.kth.id1212.hw3.common.model;

import java.io.Serializable;

public class Owner implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
