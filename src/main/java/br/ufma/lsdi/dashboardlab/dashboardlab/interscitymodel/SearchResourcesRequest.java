package br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel;

import lombok.Data;

@Data
public class SearchResourcesRequest {

    String capability;
    Double lat;
    Double lon;
    Double radius;
    String uuid;
    String description;
    String status;
    String country;
    String state;
    String city;
    String neighborhood;
    String postalCode;

}
