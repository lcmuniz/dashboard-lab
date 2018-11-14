package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Resource {

    Long id;
    String uri;
    @JsonFormat(pattern = "YYYY-mm-dd")
    LocalDate createdAt;
    @JsonFormat(pattern = "YYYY-mm-dd")
    LocalDate updatedAt;
    Double lat;
    Double lon;
    String status;
    String collectInterval;
    String description;
    String uuid;
    String city;
    String neighborhood;
    String state;
    String postalCode;
    String country;
    List<Capability> capabilities;

    public void setCapabilities(String[] capabilities) {
        this.capabilities = Arrays.stream(capabilities).map(c -> new Capability(c)).collect(Collectors.toList());
    }
}
