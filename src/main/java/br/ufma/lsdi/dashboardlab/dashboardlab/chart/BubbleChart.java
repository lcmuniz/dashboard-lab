package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BubbleChartConfig;
import com.byteowls.vaadin.chartjs.data.BubbleDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BubbleChart  {

    private static final long serialVersionUID = 2770593137320243086L;

    @Getter
    @Setter
    String title;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String group;
        Double x;
        Double y;
        Double r;
    }
    public void addData(String group, Double x, Double y, Double r) {
        data.add(new DataValue(group, x, y, r));
    }

    public BubbleChartConfig buildData() {
        BubbleChartConfig config = new BubbleChartConfig();
        List<String> labels = data.stream().map(d -> d.group).distinct().collect(Collectors.toList());
        config.data().labelsAsList(labels);
        labels.stream().forEach(dst -> {
            BubbleDataset bd = new BubbleDataset();
            bd.label(dst);
            bd.backgroundColor(ColorUtils.randomColor(0.7));
            data.stream().filter(d -> d.group.equals(dst)) .forEach(d -> {
                bd.addData(d.x, d.y, d.r);
            });
            config.data().addDataset(bd);
        });
        return config;
    }

    public Component getChart() {

        BubbleChartConfig config = buildData();

        config.
                data()
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
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