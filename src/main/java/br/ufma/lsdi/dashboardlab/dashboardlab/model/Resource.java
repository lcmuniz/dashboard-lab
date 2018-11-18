package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
public class Resource {

    String uuid;
    String description;
    String[] capabilities;
    String status;
    Double lat;
    Double lon;
    String country;
    String state;
    String city;
    String neighborhood;
    String postalCode;
    @JsonFormat(pattern = "YYYY-mm-dd")
    LocalDate createdAt;
    @JsonFormat(pattern = "YYYY-mm-dd")
    LocalDate updatedAt;
    Long id;
    Long collectInterval;
    String uri;

    public List<String> getCapabilitiesAsList() {
        return Arrays.asList(capabilities);
    }

}
