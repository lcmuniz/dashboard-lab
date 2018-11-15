package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resources;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

}
