package br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public String toString() {
        return description;
    }

}
