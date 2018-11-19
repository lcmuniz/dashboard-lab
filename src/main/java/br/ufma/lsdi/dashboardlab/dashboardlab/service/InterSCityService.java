package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.collector.DataCollectorCapability;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.collector.DataCollectorContextData;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.collector.DataCollectorResource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.collector.DataCollectorResponse;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class InterSCityService {

    String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
    //String baseUrl = "http://playground.interscity.org/";

    Gson gson = AppGson.get();

    public List<Resource> _findAllResources() {
        try {
            val url = baseUrl + "/catalog/resources";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> findAllResources() {
        int i = 1;
        List<Resource> resources = new ArrayList<>();
        while (true) {
            val page = findAllResources(i);
            if (page.size() == 0) break;
            resources.addAll(page);
            i++;
        }
        return resources;
    }

    public List<Resource> findAllResources(int page) {
        try {
            val url = baseUrl + "/catalog/resources?page="+page;
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> findAllSensorResources() {
        try {
            val url = baseUrl + "/catalog/resources/sensors";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> findAllActuatorResources() {
        try {
            val url = baseUrl + "/catalog/resources/actuators";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Resource findResource(String uuid) {

        try {
            val url = baseUrl + "catalog/resources/{uuid}";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .routeParam("uuid", uuid)
                    .asJson().getBody().toString();
            val resourceData = gson.fromJson(json, ResourceData.class);
            val resource = resourceData.getData();
            return resource;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Capability> findAllCapabilities() {
        try {
            val url = baseUrl + "/catalog/capabilities";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _capabilities = gson.fromJson(json, Capabilities.class);
            val capabilities = _capabilities.getCapabilities();
            return capabilities;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Capability> findAllSensorCapabilities() {
        try {
            val url = baseUrl + "/catalog/capabilities?capability_type=sensor";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _capabilities = gson.fromJson(json, Capabilities.class);
            val capabilities = _capabilities.getCapabilities();
            return capabilities;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Capability> findAllActuatorCapabilities() {
        try {
            val url = baseUrl + "/catalog/capabilities?capability_type=actuator";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _capabilities = gson.fromJson(json, Capabilities.class);
            val capabilities = _capabilities.getCapabilities();
            return capabilities;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Capability findCapability(String name) {
        try {
            val url = baseUrl + "catalog/capabilities/{name}";
            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .routeParam("name", name)
                    .asJson().getBody().toString();
            val capability = gson.fromJson(json, Capability.class);
            return capability;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Resource> findAllResourcesByParams(Map<String, String> params) {
        try {
            val lst = new ArrayList<String>();
            for (val entry : params.entrySet()) {
                lst.add(entry.getKey() + "=" + entry.getValue());
            }
            val p = String.join("&", lst);
            val url = baseUrl + "catalog/resources/search?" + p;

            val json = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Object> findAllDataValue(String uuid, String capability, String contextData) {

        try {
            PostDataCollector pdc = new PostDataCollector();
            pdc.setUuids(Arrays.asList(uuid));
            pdc.setCapabilities(Arrays.asList(capability));

            DataCollectorResponse response = new DataCollectorResponse(findAllData(gson.toJson(pdc)));

            List<Object> list = new ArrayList<>();
            for (DataCollectorResource dcr : response.getResources()) {
                for (DataCollectorCapability dcc : dcr.getCapabilities()) {
                    for (DataCollectorContextData dccd :dcc.getDataList()) {
                        Map<String, Object> data = dccd.getData();
                        if (data.get(contextData) != null) {
                            list.add(data.get(contextData));
                        }
                    }
                }
            }
            return list;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<List<Object>> findAllDataValue(String uuid, String capability, String[] contextData) {

        try {
            PostDataCollector pdc = new PostDataCollector();
            pdc.setUuids(Arrays.asList(uuid));
            pdc.setCapabilities(Arrays.asList(capability));

            DataCollectorResponse response = new DataCollectorResponse(findAllData(gson.toJson(pdc)));

            List<List<Object>> list = new ArrayList<>();
            for (DataCollectorResource dcr : response.getResources()) {
                for (DataCollectorCapability dcc : dcr.getCapabilities()) {
                    for (DataCollectorContextData dccd :dcc.getDataList()) {
                        Map<String, Object> data = dccd.getData();
                        List<Object> list2 = new ArrayList<>();
                        for (String s : contextData) {
                            list2.add(data.get(s));
                        }
                        list.add(list2);
                    }
                }
            }
            return list;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Resource> findAllData(PostDataCollector pdc) {

        try {
            val url = baseUrl + "collector/resources/data";
            String post = gson.toJson(pdc);
            val json = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(post).asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String findAllData(String json) {

        try {
            val url = baseUrl + "collector/resources/data";
            val result = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(json).asJson().getBody().toString();
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
