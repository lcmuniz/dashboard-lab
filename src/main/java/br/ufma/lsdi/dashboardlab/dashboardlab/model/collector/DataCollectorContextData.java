package br.ufma.lsdi.dashboardlab.dashboardlab.model.collector;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DataCollectorContextData {

    private final DataCollectorCapability capability;

    public DataCollectorContextData(DataCollectorCapability dcc) {
        this.capability = dcc;
    }

    private Map<String, Object> data;

    public DataCollectorLocation getLocation() {

        if (data.get("location") != null) {
            Map locationMap = (Map) data.get("location");
            return new DataCollectorLocation(locationMap);
        }

        if (data.get("lat") != null && data.get("lon") != null) {
            Double lat = (Double) data.get("lat");
            Double lon = (Double) data.get("lon");
            return new DataCollectorLocation(lat, lon);
        }

        if (data.get("latitude") != null && data.get("longitude") != null) {
            Double lat = (Double) data.get("latitude");
            Double lon = (Double) data.get("longitude");
            return new DataCollectorLocation(lat, lon);
        }

        return null;

    }

    public String toHTML() {
        String str = "{<br/>";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            str += "<b>" + entry.getKey() + "</b>: " + entry.getValue() + "<br />";
        }
        str += "}";
        return str;
    }
}