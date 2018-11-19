package br.ufma.lsdi.dashboardlab.dashboardlab.model.collector;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataCollectorResource {
    private String uuid;
    private List<DataCollectorCapability> capabilities = new ArrayList<>();

    public String toHTML() {
        String str = "{<br/>";
        str += "<b>" + uuid + "</b>: <br/>";
        for (DataCollectorCapability capability : capabilities) {
            str += capability.toHTML();
        }
        str += "}";
        return str;
    }
}

