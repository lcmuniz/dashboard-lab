package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.List;

public class SteppedLineChart {

    private static final long serialVersionUID = -1977315515493155463L;

    public Component getChart() {
        LineChartConfig config = new LineChartConfig();
        config.data()
                .labels("January", "February", "March", "April", "May", "June", "July")
                .addDataset(new LineDataset().label("My First dataset").fill(false).borderDash(5,5).steppedLine(true))
                .addDataset(new LineDataset().label("hidden dataset").steppedLine(true).hidden(true))
                .addDataset(new LineDataset().label("My Second dataset").steppedLine(true))
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text("Chart.js Line Chart - Stepped Line")
                .and()
                .tooltips()
                .mode(InteractionMode.INDEX)
                .and()
                .scales()
                .add(Axis.X, new CategoryScale()
                        .display(true)
                        .scaleLabel()
                        .display(true)
                        .labelString("Month")
                        .and())
                .add(Axis.Y, new LinearScale()
                        .display(true)
                        .scaleLabel()
                        .display(true)
                        .labelString("Value")
                        .and()
                        .ticks()
                        .suggestedMin(-10)
                        .suggestedMax(250)
                        .and())
                .and()
                .done();

        // add random data for demo
        List<String> labels = config.data().getLabels();
        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            LineDataset lds = (LineDataset) ds;
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) (Math.random() > 0.5 ? -1 : 1) * Math.round(Math.random() * 100));
            }
            lds.dataAsList(data);
            lds.borderColor(ColorUtils.randomColor(0.4));
            lds.backgroundColor(ColorUtils.randomColor(0.5));
            lds.pointBorderColor(ColorUtils.randomColor(0.7));
            lds.pointBackgroundColor(ColorUtils.randomColor(0.5));
            lds.pointBorderWidth(1);
        }

        ChartJs chart = new ChartJs(config);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));
        chart.setJsLoggingEnabled(true);

        chart.setSizeFull();
        return chart;
    }

}