package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Capability {

    Long id;
    String name;
    String function;
    String description;

    public Capability(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
