package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.TimeLineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.options.scale.TimeScale;
import com.byteowls.vaadin.chartjs.options.scale.TimeScaleOptions;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeLineChart implements Chart {

    private static final long serialVersionUID = -4668420742225695694L;

    @Getter @Setter
    String title;

    final List<DataValue> data = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String dataset;
        LocalDateTime time;
        Double value;
    }
    public void addData(String dataset, LocalDateTime time, Double value) {
        data.add(new DataValue(dataset, time, value));
    }

    public LineChartConfig buildData() {
        LineChartConfig config = new LineChartConfig();
        List<String> ds = data.stream().map(d -> d.dataset).distinct().collect(Collectors.toList());
        ds.stream().forEach(dst -> {
            TimeLineDataset lds = new TimeLineDataset();
            lds.label(dst);
            lds.fill(false);
            lds.backgroundColor(ColorUtils.randomColor(0.7));
            lds.borderColor(ColorUtils.randomColor(.4));
            lds.backgroundColor(ColorUtils.randomColor(.1));
            lds.pointBorderColor(ColorUtils.randomColor(.7));
            lds.pointBackgroundColor(ColorUtils.randomColor(.5));
            lds.pointBorderWidth(1);

            List<DataValue> dvl = data.stream().filter(d -> d.dataset.equals(dst)).collect(Collectors.toList());

            for(DataValue dv: dvl) {
                lds.addData(dv.time, dv.value);
            }
            config.data().addDataset(lds);
        });
        return config;
    }

    public Component getChart() {

        //LocalDateTime t = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        LocalDateTime minDate = data.stream().map(d -> d.getTime()).min(LocalDateTime::compareTo).get();
        LocalDateTime maxDate = data.stream().map(d -> d.getTime()).max(LocalDateTime::compareTo).get();

        Double minValue = data.stream().mapToDouble(d -> d.getValue()).min().getAsDouble();
        Double maxValue = data.stream().mapToDouble(d -> d.getValue()).max().getAsDouble();

        LineChartConfig config = buildData();
        config.data()
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .tooltips()
                .mode(InteractionMode.INDEX)
                .intersect(false)
                .and()
                .hover()
                .mode(InteractionMode.NEAREST)
                .intersect(true)
                .and()
                .scales()
                .add(Axis.X, new TimeScale()
                        .time()
                        .min(minDate)
                        .max(maxDate)
                        //.stepSize(2)
                        //.unit(TimeScaleOptions.Unit.HOUR)
                        .displayFormats()
                        //.hour("DD.MM HH:mm") // german date/time format
                        .and()
                        .and()
                )
                .add(Axis.Y, new LinearScale()
                        .display(true)
                        .scaleLabel()
                        .display(true)
                        .labelString("Value")
                        .and()
                        .ticks()
                        .suggestedMin(minValue)
                        .suggestedMax(maxValue)
                        .and()
                        .position(Position.LEFT))
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