package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.PieChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.chart.VerticalBarChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.byteowls.vaadin.chartjs.ChartJs;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import io.vavr.control.Option;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@StyleSheet("vaadin://styles1.css")
public class IndexView extends VerticalLayout {

    private final InterSCityService interSCityService;

    public IndexView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        GridLayout grid = new GridLayout(3,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 0.54f);
        grid.setColumnExpandRatio(1, 0.01f);
        grid.setColumnExpandRatio(2, 0.45f);
        grid.addComponent(leftSide());
        grid.addComponent(new Label(""));
        grid.addComponent(rightSide());

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private Component leftSide() {
        GridLayout statsGrid = new GridLayout(2,2);
        statsGrid.setMargin(false);
        statsGrid.setSpacing(true);
        statsGrid.setSizeFull();
        statsGrid.addComponent(panel00(), 0, 0);
        statsGrid.addComponent(panel01(), 0, 1);
        statsGrid.addComponent(panel10(), 1, 0);
        statsGrid.addComponent(panel11(), 1, 1);
        return statsGrid;
    }

    private Component rightSide() {
        LMap map = new LMap();
        map.setCenter(-2.590479, -44.224759);
        map.setZoomLevel(11.49);
        map.setSizeFull();
        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        map.addBaseLayer(layer, "OSM");

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(false);
        vl.setSpacing(false);
        vl.setSizeFull();
        vl.addComponent(map);
        return vl;
    }

    private Component panel00() {

        Label saoluis = new Label("Cidade de São Luís", ContentMode.HTML);
        saoluis.addStyleName("city");
        saoluis.addStyleName(ValoTheme.LABEL_H1);

        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();

        Label dates = new Label(dtfDate.format(now) + " | " + dtfTime.format(now));

        Label resourceLabel = new Label("266", ContentMode.HTML);
        resourceLabel.addStyleName(ValoTheme.LABEL_H1);

        Label capabilitiesLabel = new Label("12312", ContentMode.HTML);
        capabilitiesLabel .addStyleName(ValoTheme.LABEL_H1);

        HorizontalLayout hl1 = new HorizontalLayout();
        hl1.setMargin(false);
        hl1.setSpacing(false);
        hl1.setSizeFull();
        hl1.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        hl1.addComponents(resourceLabel, capabilitiesLabel);

        HorizontalLayout hl2 = new HorizontalLayout();
        hl2.setMargin(false);
        hl2.setSpacing(false);
        hl2.setSizeFull();
        hl2.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        hl2.addComponents(new Label("Capabilities"), new Label("Resources"));

        VerticalLayout vl = new VerticalLayout();
        vl.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        vl.setMargin(false);
        vl.setSpacing(false);
        vl.addComponent(saoluis);
        vl.addComponent(dates);
        vl.addComponent(new Label(""));
        vl.addComponents(hl1,hl2);

        Panel panel = new Panel("General information".toUpperCase());
        panel.setContent(vl);
        panel.setSizeFull();

        return panel;

    }

    private Component panel01() {

        val capabilities = interSCityService.getAllCapabilities(Option.none()).stream()
                .map(cap -> cap.getName())
                .sorted();
        String[] capabilitiesArr = capabilities.toArray(size -> new String[size]);

        Set<String> capabilitiesSet = new HashSet<String>();
        Random rand = new Random();
        int capNum = rand.nextInt(4) + 2;
        for (int i = 0; i < capNum; i++) {
            capabilitiesSet.add(capabilitiesArr[rand.nextInt(capabilitiesArr.length)]);
        }

        val chart = new VerticalBarChart();

        capabilitiesSet.stream().forEach(cap -> {
            val request = new SearchResourcesRequest();
            request.setCapability(cap);
            val resources = interSCityService.searchResources(request);
            resources.stream().forEach(res -> chart.addData("Resources", cap, (double) resources.size()));
        });

        Panel panel = new Panel("Resources per capability".toUpperCase());
        panel.setContent(chart.getChart());
        panel.setSizeFull();

        return panel;
    }

    private Component panel10() {
        //int sensors = interSCityService.getAllCapabilities(Option.of("sensor")).size();
        //int actuators = interSCityService.getAllCapabilities(Option.of("actuator")).size();

        int sensors = 95;
        int actuators = 5;

        PieChart pieChart = new PieChart();
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        Panel panel = new Panel("Capabilities per type".toUpperCase());
        panel.setSizeFull();
        Component chart = pieChart.getChart();
        chart.setSizeFull();
        panel.setContent(chart);

        return panel;
    }

    private Component panel11() {

        //int sensors = interSCityService.getAllResourcesWithSensorCapabilities().size();
        //int actuators = interSCityService.getAllResourcesWithActuatorCapabilities().size();

        int sensors = 90;
        int actuators = 10;

        PieChart pieChart = new PieChart();
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        Panel panel = new Panel("Resources per capability type".toUpperCase());
        panel.setContent(pieChart.getChart());
        panel.setSizeFull();
        return panel;
    }

}
