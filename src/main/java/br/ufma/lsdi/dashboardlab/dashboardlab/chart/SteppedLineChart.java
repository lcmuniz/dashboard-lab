package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
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

public class SteppedLineChart {

    private static final long serialVersionUID = -1977315515493155463L;

    @Getter
    @Setter
    String title;

    @Getter @Setter
    String XLabel = "";
    @Getter @Setter
    String YLabel = "Value";

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

    public LineChartConfig buildData() {



        LineChartConfig config = new LineChartConfig();

        List<String> labels = data.stream().map(d -> d.group).distinct().collect(Collectors.toList());
        config.data().labelsAsList(labels);
        List<String> ds = data.stream().map(d -> d.dataset).distinct().collect(Collectors.toList());
        ds.stream().forEach(dst -> {
            LineDataset bd = new LineDataset();
            bd.fill(false);
            bd.steppedLine(true);
            bd.label(dst);
            bd.borderColor(ColorUtils.randomColor(0.4));
            bd.backgroundColor(ColorUtils.randomColor(0.5));
            bd.pointBorderColor(ColorUtils.randomColor(0.7));
            bd.pointBackgroundColor(ColorUtils.randomColor(0.5));
            bd.pointBorderWidth(1);
            bd.borderColor(ColorUtils.randomColor(0.3));
            bd.backgroundColor(ColorUtils.randomColor(0.5));
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

    public Component getChart() {

        double min = data.stream().mapToDouble(d -> d.value).min().getAsDouble();
        double max = data.stream().mapToDouble(d -> d.value).max().getAsDouble();
        min = Math.floor(min / 10) * 10;
        max = Math.ceil(max / 10) * 10;
        System.out.println(min);
        System.out.println(max);

        LineChartConfig config = buildData();


        config.data().and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .tooltips()
                .mode(InteractionMode.INDEX)
                .and()
                .scales()
                .add(Axis.X, new CategoryScale()
                        .display(true)
                        .scaleLabel()
                        .display(true)
                        .labelString(XLabel)
                        .and())
                .add(Axis.Y, new LinearScale()
                        .display(true)
                        .scaleLabel()
                        .display(true)
                        .labelString(YLabel)
                        .and()
                        .ticks()
                        .suggestedMin(min)
                        .suggestedMax(max)
                        .and())
                .and()
                .done();

        ChartJs chart = new ChartJs(config);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));
        chart.setJsLoggingEnabled(true);

        chart.setSizeFull();
        return chart;
    }

}