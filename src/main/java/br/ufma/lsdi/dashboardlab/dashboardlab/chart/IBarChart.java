package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.vaadin.ui.Component;

public interface IBarChart {

    void setTitle(String title);
    void setGroupType(String groupType);
    void addData(String s, String capabilityName, Double value);
    Component getChart();

}
