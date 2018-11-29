package br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata;

import lombok.Data;

import java.util.List;

@Data
public class GetContextDataResponse {
    List<GetDataContextResource> resources;
}
