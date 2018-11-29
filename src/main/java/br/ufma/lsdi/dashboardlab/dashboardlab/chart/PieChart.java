package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PieChart {

    private static final long serialVersionUID = 4894923343920891837L;

    @Getter @Setter
    private String title;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String dataset;
        Double value;
    }
    public void addData(String dataset, Double value) {
        data.add(new DataValue(dataset, value));
    }

    public PieChartConfig buildData() {
        PieChartConfig config = new PieChartConfig();
        List<String> labels = data.stream().map(d -> d.dataset).collect(Collectors.toList());
        List<Double> values = data.stream().map(d -> d.value).collect(Collectors.toList());
        List<String> colors = data.stream().map(d -> ColorUtils.randomColor(0.7)).collect(Collectors.toList());
        config.data().labelsAsList(labels);
        PieDataset bd = new PieDataset();
        bd.dataAsList(values);
        bd.backgroundColor(colors.toArray(new String[0]));
        config.data().addDataset(bd);
        return config;
    }

    public Component getChart() {
        PieChartConfig config = buildData();
        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .animation()
                //.animateScale(true)
                .animateRotate(true)
                .and()
                .done();

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));

        chart.setSizeFull();
        return chart;
    }

}