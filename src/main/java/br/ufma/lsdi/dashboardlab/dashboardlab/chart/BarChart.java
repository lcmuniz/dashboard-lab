package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BarChart implements Chart {

    private static final long serialVersionUID = 934342877200303954L;

    @Getter @Setter
    String title;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String group;
        String dataset;
        Double value;
    }
    public void addData(String group, String dataset, Double value) {
        data.add(new DataValue(group, dataset, value));
    }

    public BarChartConfig buildData() {
        BarChartConfig config = new BarChartConfig();
        List<String> labels = data.stream().map(d -> d.group).distinct().collect(Collectors.toList());
        config.data().labelsAsList(labels);
        List<String> ds = data.stream().map(d -> d.dataset).distinct().collect(Collectors.toList());
        ds.stream().forEach(dst -> {
            BarDataset bd = new BarDataset();
            bd.label(dst);
            bd.backgroundColor(ColorUtils.randomColor(0.7));
            for (String l : labels) {
                Optional<DataValue> any = data.stream().filter(d -> d.group.equals(l) && d.dataset.equals(dst)).findAny();
                if (any.isPresent()) {
                    bd.addData(any.get().value);
                }
                else {
                    bd.addData(0);
                }
            }
            config.data().addDataset(bd);
        });
        return config;
    }

    public abstract Component getChart();

}