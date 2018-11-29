package br.ufma.lsdi.dashboardlab.dashboardlab.service;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataResponse;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.vavr.control.Option;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterSCityService {

    String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";

    Gson gson = AppGson.get();

    // GET /catalog/resources
    public List<Resource> getAllResources() {

        try {
            val url = baseUrl + "/catalog/resources";
            return findResources(url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // GET /catalog/resources/sensors
    public List<Resource> getAllResourcesWithSensorCapabilities() {

        try {
            val url = baseUrl + "/catalog/resources/sensors";
            return findResources(url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // GET /catalog/resources/actuators
    public List<Resource> getAllResourcesWithActuatorCapabilities() {

        try {
            val url = baseUrl + "/catalog/resources/actuators";
            return findResources(url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // GET /catalog/resources/search
    public List<Resource> searchResources(SearchResourcesRequest request) {

        try {

            val lst = new ArrayList<String>();

            if (request.getCapability() != null) lst.add("capability="+request.getCapability());
            if (request.getLat() != null) lst.add("lat="+request.getLat());
            if (request.getLon() != null) lst.add("lon="+request.getLon());
            if (request.getRadius() != null) lst.add("radius="+request.getRadius());
            if (request.getUuid() != null) lst.add("uuid="+request.getUuid());
            if (request.getDescription() != null) lst.add("description="+request.getDescription());
            if (request.getStatus() != null) lst.add("status="+request.getStatus());
            if (request.getCountry() != null) lst.add("country="+request.getCountry());
            if (request.getState() != null) lst.add("state="+request.getState());
            if (request.getCity() != null) lst.add("city="+request.getCity());
            if (request.getNeighborhood() != null) lst.add("neightborhood="+request.getNeighborhood());
            if (request.getPostalCode() != null) lst.add("postal_code="+request.getPostalCode());

            val queryString = String.join("&", lst);

            val url = baseUrl + "catalog/resources/search?" + queryString + "&page={page}";

            val list = new ArrayList<Resource>();
            int i = 1;
            while (true) {
                val url_ = url.replace("{page}", i+"");
                val response = Unirest.get(url_)
                        .header("accept", "application/json")
                        //.header("content-type", "application/json")
                        .asJson().getBody().toString();
                val resources = gson.fromJson(response, Resources.class);
                if (resources.getResources().size() == 0) {
                    break;
                }
                list.addAll(resources.getResources());
                i++;
            }
            return list;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // GET /catalog/resources/{uuid}
    public Resource getResource(String uuid) {

        try {
            val url = baseUrl + "/catalog/resources/{uuid}";

            val response = Unirest.get(url)
                        .header("accept", "application/json")
                        //.header("content-type", "application/json")
                        .routeParam("uuid", uuid)
                        .asJson().getBody().toString();
            val resourceData = gson.fromJson(response, ResourceData.class);
            return resourceData.getData();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // GET /catalog/capabilities
    public List<Capability> getAllCapabilities(Option<String> capabilityType) {

        try {
            val url = baseUrl + "/catalog/capabilities";

            String response;
            if (capabilityType.isDefined()) {
                response = Unirest.get(url)
                        .header("accept", "application/json")
                        //.header("content-type", "application/json")
                        .queryString("capability_type", capabilityType.get())
                        .asJson().getBody().toString();
            }
            else {
                response = Unirest.get(url)
                        .header("accept", "application/json")
                        //.header("content-type", "application/json")
                        .asJson().getBody().toString();
            }
            val capabilities = gson.fromJson(response, Capabilities.class);
            return capabilities.getCapabilities();
        }
        catch (Exception e) {
                throw new RuntimeException(e);
        }

    }

    // GET /catalog/capabilities/{name}
    public Capability getCapability(String name) {

        try {
            val url = baseUrl + "/catalog/capabilities/{name}";

            val response = Unirest.get(url)
                    .header("accept", "application/json")
                    //.header("content-type", "application/json")
                    .routeParam("name", name)
                    .asJson().getBody().toString();
            return gson.fromJson(response, Capability.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // POST /collector/resources/data
    public GetContextDataResponse getContextData(GetContextDataRequest request) {
        try {
            val url = baseUrl + "collector/resources/data";
            val jsonRequest = gson.toJson(request);
            val response = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(jsonRequest).asJson().getBody().toString();
            return gson.fromJson(response, GetContextDataResponse.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // POST /collector/resources/data/last
    public GetContextDataResponse getLastContextData(GetContextDataRequest request) {
        try {
            val url = baseUrl + "collector/resources/data/last";
            val jsonRequest = gson.toJson(request);
            val response = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(jsonRequest).asJson().getBody().toString();
            return gson.fromJson(response, GetContextDataResponse.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // GET /discovery/resources
    public List<Resource> discoveryResources(DiscoveryResourcesRequest request) {

        try {

            val lst = new ArrayList<String>();

            if (request.getCapability() != null) lst.add("capability="+request.getCapability());
            if (request.getLat() != null) lst.add("lat="+request.getLat());
            if (request.getLon() != null) lst.add("lon="+request.getLon());
            if (request.getRadius() != null) lst.add("radius="+request.getRadius());
            lst.addAll(request.getMatchers());

            val queryString = String.join("&", lst);

            val url = baseUrl + "discovery/resources?" + queryString;

            val response = Unirest.get(url)
                    .header("accept", "application/json")
                    //.header("Content-Type", "application/json")
                    .asJson().getBody().toString();
            val resources = gson.fromJson(response, Resources.class);
            return resources.getResources();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private List<Resource> findResources(String url) throws UnirestException {
        val list = new ArrayList<Resource>();
        int i = 1;
        while (true) {
            val response = Unirest.get(url)
                    .header("accept", "application/json")
                    //.header("content-type", "application/json")
                    .queryString("page", i)
                    .asJson().getBody().toString();
            val resources = gson.fromJson(response, Resources.class);
            if (resources.getResources().size() == 0) {
                break;
            }
            list.addAll(resources.getResources());
            i++;
        }
        return list;
    }

}
