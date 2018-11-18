package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.*;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InterSCityService {

    RestTemplate restTemplate = new RestTemplate();

    String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
    //String baseUrl = "http://playground.interscity.org/";

    Gson gson = new Gson();

    public List<Resource> findAllResources() {
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

    public List<Resource> findAllData(PostDataCollector pdc) {

        try {
            val url = baseUrl + "collector/resources/data";
            String post = gson.toJson(pdc);
            val json = Unirest.post(url)
                    .body(post).asJson().getBody().toString();
            val _resources = gson.fromJson(json, Resources.class);
            val resources = _resources.getResources();
            return resources;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
