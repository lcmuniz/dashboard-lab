package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BubbleChartConfig;
import com.byteowls.vaadin.chartjs.data.BubbleDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.ui.Component;

public class BubbleChart  {

    private static final long serialVersionUID = 2770593137320243086L;

    public Component getChart() {

        BubbleChartConfig config = new BubbleChartConfig();
        config.
                data()
                .addDataset(new BubbleDataset().label("My First dataset"))
                .addDataset(new BubbleDataset().label("My Second dataset"))
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text("Chart.js Bubble Chart")
                .and()
                .done();

        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            BubbleDataset lds = (BubbleDataset) ds;
            lds.backgroundColor(ColorUtils.randomColor(.7));
            for (int i = 0; i < 15; i++) {
                lds.addData(ChartUtils.randomScalingFactor(), ChartUtils.randomScalingFactor(), Math.abs(ChartUtils.randomScalingFactor()) / 5);
            }
        }

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));

        chart.setSizeFull();
        return chart;
    }

}