package br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GetContextDataRequest {
    List<String> uuids = new ArrayList<>();
    List<String> capabilities = new ArrayList<>();
    Map<String, Object> matchers = new HashMap<>();
    LocalDateTime startDate;
    LocalDateTime endDate;
}
