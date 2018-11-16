package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class Resource {

    String uuid;
    String description;
    List<Capability> capabilities;
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

    public void setCapabilities(String[] capabilities) {
        this.capabilities = Arrays.stream(capabilities).map(c -> new Capability(c)).collect(Collectors.toList());
    }
}
