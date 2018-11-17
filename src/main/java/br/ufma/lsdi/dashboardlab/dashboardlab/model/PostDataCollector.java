package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PostDataCollector {
    List<String> uuids;
    List<String> capabilities;
    String matchers;
    LocalDate startRange;
    LocalDate endRange;
}
