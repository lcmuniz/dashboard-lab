package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class VerticalBarChart {

    private static final long serialVersionUID = 934342877200303954L;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String[] labels;

    @Getter @Setter
    private List<Double> data;

    @Getter @Setter
    private String yLabel;

    @Getter @Setter
    private String datasetLabel;

    public Component getChart() {
        BarChartConfig barConfig = new BarChartConfig();
        barConfig.
                data()
                .labels(labels)
                //.addDataset(new BarDataset().backgroundColor("rgba(220,220,220,0.5)").label("Dataset 1").yAxisID("y-axis-1"))
                //.addDataset(new BarDataset().backgroundColor("rgba(151,187,205,0.5)").label("Dataset 2").yAxisID("y-axis-2").hidden(true))
                .addDataset(
                        new BarDataset().backgroundColor(
                                //ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7),
                                //ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7), ColorUtils.randomColor(0.7),
                                ColorUtils.randomColor(0.7)).label(datasetLabel).yAxisID("y-axis-1"))
                .and();
        barConfig.
                options()
                .responsive(true)
                .hover()
                .mode(InteractionMode.INDEX)
                .intersect(true)
                .animationDuration(400)
                .and()
                .title()
                .display(true)
                .text(title)
                .and()
                .scales()
                .add(Axis.Y, new LinearScale().display(true).position(Position.LEFT).id("y-axis-1").scaleLabel().display(true).labelString(yLabel).and())
                //.add(Axis.Y, new LinearScale().display(true).position(Position.RIGHT).id("y-axis-2").gridLines().drawOnChartArea(false).and())
                .and()
                .done();

        List<String> labels = barConfig.data().getLabels();
        for (Dataset<?, ?> ds : barConfig.data().getDatasets()) {
            BarDataset lds = (BarDataset) ds;
            //List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add(data.get(i));
            }
            lds.dataAsList(data);
        }

        ChartJs chart = new ChartJs(barConfig);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) -> ChartUtils.notification(a, b, barConfig.data().getDatasets().get(a)));
        chart.addLegendClickListener((dataSetIndex,visible, visibleDatasets) -> ChartUtils.legendNotification(dataSetIndex, visible, visibleDatasets));
        chart.setSizeFull();
        return chart;
    }

}