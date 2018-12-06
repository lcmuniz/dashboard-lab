package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BubbleChartConfig;
import com.byteowls.vaadin.chartjs.data.BubbleDataset;
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
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BubbleChart  {

    private static final long serialVersionUID = 2770593137320243086L;

    double minx = 999999999999999999999999999999999f;
    double maxx = -999999999999999999999999999999999f;
    double miny = 999999999999999999999999999999999f;
    double maxy = -999999999999999999999999999999999f;

    @Getter
    @Setter
    String title;

    @Getter
    @Setter
    String groupType;

    @Getter
    @Setter
    String x;

    @Getter
    @Setter
    String y;

    final List<DataValue> rawdata = new ArrayList<>();
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
        rawdata.add(new DataValue(group, x, y, r));
    }

    public BubbleChartConfig buildData() {

        Map<String, DoubleSummaryStatistics> map1 = rawdata.stream().collect(Collectors.groupingBy(DataValue::getGroup,
                Collectors.summarizingDouble(DataValue::getX)));

        Map<String, DoubleSummaryStatistics> map2 = rawdata.stream().collect(Collectors.groupingBy(DataValue::getGroup,
                Collectors.summarizingDouble(DataValue::getY)));

        Map<String, List<DataValue>> map3 = rawdata.stream().collect(Collectors.groupingBy(DataValue::getGroup));

        for (String s1 : map3.keySet()) {
            double x1 = 0, y1 = 0;
            List<DataValue> dv = map3.get(s1);
            if (groupType.equals("Sum")) {
                x1 = map1.get(s1).getSum();
                y1 = map2.get(s1).getSum();
            } else if (groupType.equals("Count")) {
                x1 = map1.get(s1).getCount();
                y1 = map2.get(s1).getCount();
            } else if (groupType.equals("Avg")) {
                x1 = map1.get(s1).getAverage();
                y1 = map2.get(s1).getAverage();
            } else if (groupType.equals("Min")) {
                x1 = map1.get(s1).getMin();
                y1 = map2.get(s1).getMin();
            } else if (groupType.equals("Max")) {
                x1 = map1.get(s1).getMax();
                y1 = map2.get(s1).getMax();
            }

            data.add(new DataValue(s1, x1, y1, (double) dv.get(0).r / 1000));
            if (x1 < minx) minx = x1;
            if (x1 > maxx) maxx = x1;
            if (y1 < miny) miny = y1;
            if (y1 > maxy) maxy = y1;

        }

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
                .scales()
                .add(Axis.X, new LinearScale().display(true).scaleLabel().display(true).labelString(x).and().position(Position.BOTTOM).gridLines().zeroLineColor("rgba(0,0,0,1)").and().ticks().suggestedMin(minx - 1).suggestedMax(maxx + 1).and())
                .add(Axis.Y, new LinearScale().display(true).scaleLabel().display(true).labelString(y).and().position(Position.LEFT).id("y-axis-1").scaleLabel().and().ticks().suggestedMin(miny - 1).suggestedMax(maxy + 1).and())
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