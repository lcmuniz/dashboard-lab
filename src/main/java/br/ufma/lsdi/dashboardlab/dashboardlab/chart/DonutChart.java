package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.DonutChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.List;

public class DonutChart {

    private static final long serialVersionUID = 4894923343920891837L;

    public Component getChart() {
        DonutChartConfig config = new DonutChartConfig();
        config
                .data()
                .labels("Red", "Green", "Yellow", "Grey", "Dark Grey")
                .addDataset(new PieDataset().label("Dataset 1"))
                .addDataset(new PieDataset().label("Dataset 2"))
                .addDataset(new PieDataset().label("Dataset 3"))
                .and();

        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .text("Chart.js Doughnut Chart")
                .and()
                .animation()
                .animateScale(true)
                .animateRotate(true)
                .and()
                .done();

        String[] colors = new String[] {"#F7464A", "#46BFBD", "#FDB45C", "#949FB1", "#4D5360"};

        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            PieDataset lds = (PieDataset) ds;
            lds.backgroundColor(colors);
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.round(Math.random() * 100)));
            }
            lds.dataAsList(data);
        }

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) -> ChartUtils.notification(a, b, config.data().getDatasets().get(a)));
        chart.setSizeFull();
        return chart;
    }

}
