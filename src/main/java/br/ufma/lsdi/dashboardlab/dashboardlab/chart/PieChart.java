package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.PieChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.PieDataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PieChart {

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String[] labels;

    @Getter @Setter
    private List<Double> data;

    private static final long serialVersionUID = 4894923343920891837L;

    public Component getChart() {
        PieChartConfig config = new PieChartConfig();
        config
                .data()
                .labels(labels)
                .addDataset(new PieDataset().label("Dataset 1"))
                .and();

        config.
                options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .animation()
                //.animateScale(true)
                .animateRotate(true)
                .and()
                .done();

        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            PieDataset lds = (PieDataset) ds;
            List<Double> datas = new ArrayList<>();
            List<String> colors = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                datas.add(data.get(i));
                colors.add(ColorUtils.randomColor(0.7));
            }
            lds.backgroundColor(colors.toArray(new String[colors.size()]));
            lds.dataAsList(datas);
        }

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));

        chart.setSizeFull();
        return chart;
    }

}