package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.*;
import com.vaadin.ui.Component;

import java.util.List;

public class BasicLine {

    private final String capability;
    private final String x;
    private final String[] ys;
    private final List<List<Object>> values;

    public BasicLine(String capability, String x, String ys, List<List<Object>> values) {
        this.capability = capability;
        this.x = x;
        this.ys = ys.split(",");
        this.values = values;
    }

    public String getDescription() {
        return "Basic Line With Data Labels";
    }

    public Component getChart() {
        Chart chart = new Chart();
        chart.setHeight("450px");
        chart.setWidth("100%");

        Configuration configuration = chart.getConfiguration();
        configuration.getChart().setType(ChartType.LINE);
        configuration.getChart().setMarginRight(130);
        configuration.getChart().setMarginBottom(25);

        configuration.getTitle().setText(capability.toUpperCase());
        configuration.getSubTitle().setText("Source: InterSCity Plataform");

        for(List<Object> value : values) {
            configuration.getxAxis().addCategory(value.get(0)+"");
        }

        YAxis yAxis = configuration.getyAxis();
        yAxis.setMin(-5d);
        yAxis.setTitle(new AxisTitle(ys[0].toUpperCase()));
        yAxis.getTitle().setAlign(VerticalAlign.MIDDLE);

        configuration
                .getTooltip()
                .setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>'+this.x +': '+ this.y");

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.getDataLabels().setEnabled(true);
        configuration.setPlotOptions(plotOptions);

        Legend legend = configuration.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setAlign(HorizontalAlign.RIGHT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(-10d);
        legend.setY(100d);
        legend.setBorderWidth(0);


        for (int i = 0; i < ys.length; i++) {
            ListSeries ls = new ListSeries();
            ls.setName(ys[i]);
            for (List<Object> value : values) {
                ls.addData((Number) value.get(i+1));
            }
            configuration.addSeries(ls);
        }

        chart.drawChart(configuration);
        return chart;
    }

}