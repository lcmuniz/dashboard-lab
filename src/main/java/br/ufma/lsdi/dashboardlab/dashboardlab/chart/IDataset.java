package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import java.time.LocalDateTime;
import java.util.List;

public interface IDataset {
    String getLabel();
    List<Double> getData();
    List<LocalDateTime> getDate();
}
