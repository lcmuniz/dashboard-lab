package br.ufma.lsdi.dashboardlab.dashboardlab.model.collector;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataCollectorLocation {

    Gson gson = AppGson.get();

    @Getter
    private Double lat;
    @Getter
    private Double lon;

    public DataCollectorLocation(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public DataCollectorLocation(Map map) {

        if (map.get("lat") != null) lat = (Double) map.get("lat");
        else if (map.get("latitude") != null) lat = (Double) map.get("latitude");

        if (map.get("lon") != null) lon = (Double) map.get("lon");
        else if (map.get("longitude") != null) lon = (Double) map.get("longitude");

    }

    private Map<String, Object> getLocationJson(String json) {
        return gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
    }
}
