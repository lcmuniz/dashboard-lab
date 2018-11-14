package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;

@Data
public class Capability {

    String name;

    public Capability(String name) {
        this.name = name;
    }

}
