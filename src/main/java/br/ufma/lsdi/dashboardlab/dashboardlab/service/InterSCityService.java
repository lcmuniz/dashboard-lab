package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InterSCityService {

    RestTemplate restTemplate = new RestTemplate();

    String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
    //String baseUrl = "http://playground.interscity.org/";

    public List<Resource> findAllResources() {
        String url = baseUrl + "/catalog/resources";
        return restTemplate.getForObject(url, Resources.class).getResources();
    }

    public List<Resource> findAllSensorResources() {
        String url = baseUrl + "/catalog/resources/sensors";
        return restTemplate.getForObject(url, Resources.class).getResources();
    }

    public List<Resource> findAllActuatorResources() {
        String url = baseUrl + "/catalog/resources/actuators";
        return restTemplate.getForObject(url, Resources.class).getResources();
    }

    public Resource findResource(String uuid) {
        String url = baseUrl + "catalog/resources/{uuid}";
        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);
        ResourceData resourceData = restTemplate.getForObject(url, ResourceData.class, params);
        return resourceData.getData();
    }

    public List<Capability> findAllCapabilities() {
        String url = baseUrl + "/catalog/capabilities";
        return restTemplate.getForObject(url, Capabilities.class).getCapabilities();
    }

    public List<Capability> findAllSensorCapabilities() {
        String url = baseUrl + "/catalog/capabilities?capability_type=sensor";
        return restTemplate.getForObject(url, Capabilities.class).getCapabilities();
    }

    public List<Capability> findAllActuatorCapabilities() {
        String url = baseUrl + "/catalog/capabilities?capability_type=actuator";
        return restTemplate.getForObject(url, Capabilities.class).getCapabilities();
    }

    public Capability findCapability(String name) {
        String url = baseUrl + "catalog/capabilities/{name}";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return restTemplate.getForObject(url, Capability.class, params);
    }

    public List<Resource> findAllResourcesByParams(Map<String, String> params) {
        String url = baseUrl + "catalog/resources/search?";

        List<String> lst = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            lst.add(entry.getKey() + "=" + entry.getValue());
        }
        String p = String.join("&", lst);
        url += p;

        return restTemplate.getForObject(url, Resources.class).getResources();
    }

    public List<Resource> findAllData(PostDataCollector pdc) {
        String url = baseUrl + "collector/resources/data";
        HttpEntity<PostDataCollector> request = new HttpEntity<>(pdc);
        return restTemplate.postForObject(url, request, Resources.class).getResources();
    }
}
