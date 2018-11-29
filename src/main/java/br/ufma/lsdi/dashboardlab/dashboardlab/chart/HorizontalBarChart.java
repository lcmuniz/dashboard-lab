package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.elements.Rectangle;
import com.vaadin.ui.Component;

public class HorizontalBarChart extends BarChart {

    public Component getChart() {

        BarChartConfig barConfig = buildData();
        barConfig.horizontal();

        barConfig.
                data()
                .and()
                .options()
                .responsive(true)
                .title()
                .display(true)
                .text(title)
                .and()
                .elements()
                .rectangle()
                .borderWidth(2)
                .borderColor("rgb(0, 255, 0)")
                .borderSkipped(Rectangle.RectangleEdge.LEFT)
                .and()
                .and()
                .legend()
                .fullWidth(false)
                .position(Position.LEFT)
                .and()
                .done();

        ChartJs chart = new ChartJs(barConfig);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) -> ChartUtils.notification(a, b, barConfig.data().getDatasets().get(a)));
        chart.setSizeFull();
        return chart;
    }

}