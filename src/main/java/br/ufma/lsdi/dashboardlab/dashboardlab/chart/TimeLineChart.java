package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.GroupTimeType;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SummaryType;
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
import com.vaadin.ui.VerticalLayout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TimeLineChart {

    private static final long serialVersionUID = -4668420742225695694L;

    @Getter @Setter
    String title;

    @Getter @Setter
    String groupType; // sum, min, max...

    @Getter @Setter
    String groupTime; // hour, day, month year

    final List<DataValue> data = new ArrayList<>();
    final List<DataValue> rawdata = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String dataset;
        LocalDateTime time;
        Double value;
    }
    public void addData(String dataset, LocalDateTime time, Double value) {
        rawdata.add(new DataValue(dataset, time, value));
    }

    public LineChartConfig buildData() {



        Map<String, Map<LocalDateTime, DoubleSummaryStatistics>> map1 = createMap();

        for (String s1 : map1.keySet()) {
            Map<LocalDateTime, DoubleSummaryStatistics> map2 = map1.get(s1);
            for (LocalDateTime s2 : map2.keySet()) {
                if (groupType.equals(SummaryType.SUM)) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getSum()));
                } else if (groupType.equals(SummaryType.COUNT)) {
                    data.add(new DataValue(s1, s2, (double) map2.get(s2).getCount()));
                } else if (groupType.equals(SummaryType.AVERAGE)) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getAverage()));
                } else if (groupType.equals(SummaryType.MIN)) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getMin()));
                } else if (groupType.equals(SummaryType.MAX)) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getMax()));
                }
            }
        }

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

            List<DataValue> dvl = data.stream().filter(d -> d.dataset.equals(dst)).sorted(Comparator.comparing(DataValue::getTime)).collect(Collectors.toList());

            for(DataValue dv: dvl) {
                lds.addData(dv.time, dv.value);
            }
            config.data().addDataset(lds);
        });
        return config;
    }

    private Map<String, Map<LocalDateTime, DoubleSummaryStatistics>> createMap() {
        if (groupTime.equals(GroupTimeType.HOUR)) {
            return rawdata.stream().collect(
                            Collectors.groupingBy(DataValue::getDataset,
                                    Collectors.groupingBy(d -> d.getTime().withMinute(1).withSecond(1).withNano(1),
                                            Collectors.summarizingDouble(DataValue::getValue))));
        }
        else if (groupTime.equals(GroupTimeType.DAY)) {
            return rawdata.stream().collect(
                    Collectors.groupingBy(DataValue::getDataset,
                            Collectors.groupingBy(d -> d.getTime().withHour(1).withMinute(1).withSecond(1).withNano(1),
                                    Collectors.summarizingDouble(DataValue::getValue))));
        }
        else if (groupTime.equals(GroupTimeType.MONTH)) {
            return rawdata.stream().collect(
                    Collectors.groupingBy(DataValue::getDataset,
                            Collectors.groupingBy(d -> d.getTime().withDayOfMonth(1).withHour(1).withMinute(1).withSecond(1).withNano(1),
                                    Collectors.summarizingDouble(DataValue::getValue))));
        }
        else if (groupTime.equals(GroupTimeType.YEAR)) {
            return rawdata.stream().collect(
                    Collectors.groupingBy(DataValue::getDataset,
                            Collectors.groupingBy(d -> d.getTime().withMonth(1).withDayOfMonth(1).withHour(1).withMinute(1).withSecond(1).withNano(1),
                                    Collectors.summarizingDouble(DataValue::getValue))));
        }
        else {
            return rawdata.stream().collect(
                    Collectors.groupingBy(DataValue::getDataset,
                            Collectors.groupingBy(d -> d.getTime(),
                                    Collectors.summarizingDouble(DataValue::getValue))));
        }

    }

    public Component getChart() {

        //LocalDateTime t = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        LocalDateTime minDate = rawdata.stream().map(d -> d.getTime()).min(LocalDateTime::compareTo).get();
        LocalDateTime maxDate = rawdata.stream().map(d -> d.getTime()).max(LocalDateTime::compareTo).get();

        Double minValue = rawdata.stream().mapToDouble(d -> d.getValue()).min().getAsDouble();
        Double maxValue = rawdata.stream().mapToDouble(d -> d.getValue()).max().getAsDouble();

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
                        .unit(getTimeScale())
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

    private TimeScaleOptions.Unit getTimeScale() {
        if (groupTime.equals(GroupTimeType.HOUR)) {
            return TimeScaleOptions.Unit.HOUR;
        }
        else if (groupTime.equals(GroupTimeType.DAY)) {
            return TimeScaleOptions.Unit.DAY;
        }
        else if (groupTime.equals(GroupTimeType.MONTH)) {
            return TimeScaleOptions.Unit.MONTH;
        }
        else if (groupTime.equals(GroupTimeType.YEAR)) {
            return TimeScaleOptions.Unit.YEAR;
        }
        else {
            return TimeScaleOptions.Unit.MINUTE;
        }
    }

}