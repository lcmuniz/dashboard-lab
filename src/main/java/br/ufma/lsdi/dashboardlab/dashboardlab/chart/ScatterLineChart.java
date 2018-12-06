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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScatterLineChart {

    private static final long serialVersionUID = -4668420742225695694L;

    double minx = 999999999999999999999999999999999f;
    double maxx = -999999999999999999999999999999999f;
    double miny = 999999999999999999999999999999999f;
    double maxy = -999999999999999999999999999999999f;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String x;

    @Getter
    @Setter
    String y;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        Object group;
        String capability;
        Double value;
    }

    public void addData(Object group, String capability, Double value) {
        data.add(new DataValue(group, capability, value));
    }

    public ScatterChartConfig buildData() {

        ScatterDataset lds = new ScatterDataset();
        lds.label(x + " x " + y);
        lds.borderColor(ColorUtils.randomColor(.4));
        lds.backgroundColor(ColorUtils.randomColor(.1));
        lds.pointBorderColor(ColorUtils.randomColor(.7));
        lds.pointBackgroundColor(ColorUtils.randomColor(.5));
        lds.pointBorderWidth(1);

        Map<Object, List<DataValue>> g = data.stream().collect(Collectors.groupingBy(DataValue::getGroup));
        for(Object l : g.keySet()) {

            List<DataValue> list = g.get((l));
            Optional<DataValue> ox = list.stream().filter(rd -> rd.getCapability().equals(x)).findFirst();
            Optional<DataValue> oy = list.stream().filter(rd -> rd.getCapability().equals(y)).findFirst();

            double xv = ox.isPresent() ? ox.get().value : 0;
            double yv = oy.isPresent() ? oy.get().value : 0;

            if (xv < minx) minx = xv;
            if (xv > maxx) maxx = xv;
            if (yv < miny) miny = yv;
            if (yv > maxy) maxy = yv;

            lds.addData(xv, yv);
        }

        ScatterChartConfig config = new ScatterChartConfig();
        config.data().addDataset(lds);

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
                .add(Axis.X, new LinearScale().display(true).scaleLabel().display(true).labelString(x).and().position(Position.BOTTOM).gridLines().zeroLineColor("rgba(0,0,0,1)").and().ticks().suggestedMin(minx - 10).suggestedMax(maxx + 10).and())
                .add(Axis.Y, new LinearScale().display(true).scaleLabel().display(true).labelString(y).and().position(Position.LEFT).id("y-axis-1").scaleLabel().and().ticks().suggestedMin(miny - 10).suggestedMax(maxy + 10).and())
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