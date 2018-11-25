package br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiscoveryResourcesRequest {

    String capability;
    Double lat;
    Double lon;
    Double radius;
    List<String> matchers = new ArrayList<>();

}
