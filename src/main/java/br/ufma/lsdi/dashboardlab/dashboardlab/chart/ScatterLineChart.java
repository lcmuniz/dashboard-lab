package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.ScatterChartConfig;
import com.byteowls.vaadin.chartjs.data.ScatterDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScatterLineChart {

    private static final long serialVersionUID = -4668420742225695694L;

    @Getter
    @Setter
    String title;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String dataset;
        Double x;
        Double y;
    }
    public void addData(String dataset, Double x, Double y) {
        data.add(new DataValue(dataset, x, y));
    }

    public ScatterChartConfig buildData() {
        ScatterChartConfig config = new ScatterChartConfig();

        List<String> datasets = data.stream().map(d -> d.dataset).distinct().collect(Collectors.toList());
        datasets.stream().forEach(ds -> {
            ScatterDataset lds = new ScatterDataset();
            lds.label(ds);
            lds.borderColor(ColorUtils.randomColor(.4));
            lds.backgroundColor(ColorUtils.randomColor(.1));
            lds.pointBorderColor(ColorUtils.randomColor(.7));
            lds.pointBackgroundColor(ColorUtils.randomColor(.5));
            lds.pointBorderWidth(1);

            List<DataValue> datas = data.stream().filter(d -> d.getDataset().equals(ds)).collect(Collectors.toList());
            for (DataValue dv : datas) {
                lds.addData(dv.x, dv.y);
            }
            config.data().addDataset(lds);
        });

        return config;
    }

    public Component getChart() {
        ScatterChartConfig config = buildData();
        config.
                options()
                .responsive(true)
                .hover()
                .mode(InteractionMode.NEAREST)
                .intersect(false)
                .and()
                .title()
                .display(true)
                .text(title)
                .and()
                .scales()
                .add(Axis.X, new LinearScale().position(Position.BOTTOM).gridLines().zeroLineColor("rgba(0,0,0,1)").and())
                .add(Axis.Y, new LinearScale().display(true).position(Position.LEFT).id("y-axis-1"))
                //.add(Axis.Y, new LinearScale().display(true).position(Position.RIGHT).id("y-axis-2").ticks().reverse(true).and().gridLines().drawOnChartArea(false).and())
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