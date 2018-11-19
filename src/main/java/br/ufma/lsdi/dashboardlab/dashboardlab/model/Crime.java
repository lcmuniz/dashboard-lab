package br.ufma.lsdi.dashboardlab.dashboardlab.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Crime {
    private Double lat;
    private Double lon;
    private LocalDateTime timestamp;
}
