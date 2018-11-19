package br.ufma.lsdi.dashboardlab.dashboardlab.model.collector;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataCollectorResponse {

    Gson gson = AppGson.get();

    @Getter
    List<DataCollectorResource> resources = new ArrayList<>();

    public DataCollectorResponse(String json) {
        ArrayList resources = getResourcesJson(json);
        for (int i = 0; i < resources.size(); i++) {
            Map resource = (Map) resources.get(i);
            DataCollectorResource dcr = new DataCollectorResource();
            dcr.setUuid((String) resource.get("uuid"));
            Map capabilities = (Map) resource.get("capabilities");
            capabilities.forEach((key,value)-> {
                DataCollectorCapability dcc = new DataCollectorCapability(dcr);
                dcc.setName((String) key);
                ArrayList contextDataList = (ArrayList) value;
                for (int j = 0; j < contextDataList.size(); j++) {
                    DataCollectorContextData dccd = new DataCollectorContextData(dcc);
                    dccd.setData((Map) contextDataList.get(j));
                    dcc.getDataList().add(dccd);
                }
                dcr.getCapabilities().add(dcc);
            });

            this.resources.add(dcr);
        }
    }

    private ArrayList getResourcesJson(String json) {
        Map<String, List<Object>> map = gson.fromJson(json, new TypeToken<Map<String, List<Object>>>(){}.getType());
        return (ArrayList) map.get("resources");
    }

}

