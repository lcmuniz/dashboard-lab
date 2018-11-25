package br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class Capability {

    Long id;
    String name;
    String function;
    String description;
    String capabilityType;

}
