package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public List<Resource> findAllSensors() {
        String url = baseUrl + "/catalog/resources/sensors";
        return restTemplate.getForObject(url, Resources.class).getResources();
    }

    public List<Resource> findAllActuators() {
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

    public Capability findCapability(String name) {
        String url = baseUrl + "catalog/capabilities/{name}";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        Capability capability= restTemplate.getForObject(url, Capability.class, params);
        return capability;
    }

}
