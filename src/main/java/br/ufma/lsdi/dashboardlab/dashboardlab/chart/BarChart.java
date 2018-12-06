package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BarChart implements IBarChart {

    private static final long serialVersionUID = 934342877200303954L;

    @Getter @Setter
    String title;

    @Getter @Setter
    String groupType;

    final List<DataValue> data = new ArrayList<>();
    final List<DataValue> rawdata = new ArrayList<>();

    @Data
    @AllArgsConstructor
    class DataValue {
        String group;
        String dataset;
        Double value;
    }
    public void addData(String group, String dataset, Double value) {
        rawdata.add(new DataValue(group, dataset, value));
    }

    public BarChartConfig buildData() {

        Map<String, Map<String, DoubleSummaryStatistics>> map1 =
                rawdata.stream().collect(
                        Collectors.groupingBy(DataValue::getGroup,
                                Collectors.groupingBy(DataValue::getDataset,
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