package br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetDataContextResource {
    String uuid;
    Map<String, List<Map<String, Object>>> capabilities;
}
