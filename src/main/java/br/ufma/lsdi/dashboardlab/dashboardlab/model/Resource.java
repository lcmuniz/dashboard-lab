package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Resource {

    Long id;
    String uri;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Double lat;
    Double lon;
    String status;
    Long collectInterval;
    String description;
    String uuid;
    String city;
    String neighborhood;
    String state;
    String postalCode;
    String country;
    List<String> capabilities;
    Double distance;
    Double bearing;


    @Override
    public String toString() {
        return description;
    }

    public String toHTML() {
        String html = "<p>";

        html += "<b>UUID</b>: " + uuid + "<br/>";
        html += "<b>Description</b>: " + description + "<br/>";
        html += "<b>ID</b>: " + id + "<br/>";
        if (uri != null) html += "<b>URI: " + uri + "<br/>";
        html += "<b>Latitude</b>: " + lat + "<br/>";
        html += "<b>Longitude</b>: " + lon + "<br/>";
        html += "<b>Status</b>: " + status + "<br/>";

        List<String> caps10 = capabilities.stream().limit(10).collect(Collectors.toList());
        if (caps10.size() > 0) html += "<b>Capabilities</b>: " + caps10 + "<br/>";

        if (collectInterval != null) html += "<b>Collect interval</b>: " + collectInterval + "<br/>";
        if (city!= null) html += "<b>City</b>: " + city + "<br/>";
        if (neighborhood != null) html += "<b>Neighborhood</b>: " + neighborhood + "<br/>";
        if (state != null) html += "<b>State</b>: " + state + "<br/>";
        if (postalCode!= null) html += "<b>Postal code</b>: " + postalCode + "<br/>";
        if (country != null) html += "<b>Country</b>: " + country + "<br/>";
        if (distance != null) html += "<b>Distance</b>: " + distance + "<br/>";
        if (bearing != null) html += "<b>Baraing</b>: " + bearing + "<br/>";
        if (createdAt != null) html += "<b>Created at</b>: " + createdAt + "<br/>";
        if (updatedAt != null) html += "<b>Updated at</b>: " + updatedAt + "<br/>";

        html += "</p>";
        return html;
    }

}
