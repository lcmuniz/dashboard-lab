package br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.contextdata;

import lombok.Data;

import java.util.List;

@Data
public class GetContextDataResponse {
    List<GetDataContextResource> resources;
}
