package br.ufma.lsdi.dashboardlab.dashboardlab.model.collector;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public @Data
class DataCollectorCapability {
    private final DataCollectorResource resource;
    private String name;
    private List<DataCollectorContextData> dataList = new ArrayList();

    public DataCollectorCapability(DataCollectorResource dcr) {
        this.resource = dcr;
    }

    public String toHTML() {
        String str = "{<br/>";
        str += "<b>" + name + "</b>: <br/>";
        for (DataCollectorContextData data : dataList) {
            str += data.toHTML();
        }
        str += "}";
        return str;
    }

}