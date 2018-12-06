package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.DonutChartConfig;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public class DonutChart {

    private static final long serialVersionUID = 4894923343920891837L;

    final List<DataValue> rawdata = new ArrayList<>();
    final List<DataValue> data = new ArrayList<>();

    @Getter @Setter
    String groupType;

    @Getter
    @Setter
    String title;

    @Data
    @AllArgsConstructor
    class DataValue {
        String dataset;
        String group;
        Double value;
    }
    public void addData(String dataset, String group, Double value) {
        rawdata.add(new DataValue(dataset, group, value));
    }

    public DonutChartConfig buildData() {

        Map<String, Map<String, DoubleSummaryStatistics>> map1 =
                rawdata.stream().collect(
                        Collectors.groupingBy(DataValue::getDataset,
                                Collectors.groupingBy(DataValue::getGroup,
                                        Collectors.summarizingDouble(DataValue::getValue))));

        for (String s1 : map1.keySet()) {
            Map<String, DoubleSummaryStatistics> map2 = map1.get(s1);
            for (String s2 : map2.keySet()) {
                if (groupType.equals("Sum")) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getSum()));
                } else if (groupType.equals("Count")) {
                    data.add(new DataValue(s1, s2, (double) map2.get(s2).getCount()));
                } else if (groupType.equals("Avg")) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getAverage()));
                } else if (groupType.equals("Min")) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getMin()));
                } else if (groupType.equals("Max")) {
                    data.add(new DataValue(s1, s2, map2.get(s2).getMax()));
                }
            }
        }

        DonutChartConfig config = new DonutChartConfig();
        List<String> labels = data.stream().map(d -> d.group).distinct().collect(Collectors.toList());
        config.data().labelsAsList(labels);
        List<String> ds = data.stream().map(d -> d.dataset).distinct().collect(Collectors.toList());

        List<String> colors = labels.stream().map(l -> ColorUtils.randomColor(.7)).collect(Collectors.toList());

        ds.stream().forEach(dst -> {
            PieDataset bd = new PieDataset();
            bd.label(dst);
            bd.backgroundColor(colors.toArray(new String[0]));
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

        DonutChartConfig config = buildData();

        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .animation()
                .animateScale(true)
                .animateRotate(true)
                .and()
                .done();

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) -> ChartUtils.notification(a, b, config.data().getDatasets().get(a)));
        chart.setSizeFull();
        return chart;
    }

}
