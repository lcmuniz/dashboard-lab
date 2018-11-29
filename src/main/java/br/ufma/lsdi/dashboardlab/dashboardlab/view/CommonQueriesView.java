package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.PieChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.chart.VerticalBarChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import io.vavr.control.Option;
import lombok.val;

import java.util.Arrays;
import java.util.Set;

public class CommonQueriesView extends VerticalLayout {

    private final InterSCityService interSCityService;

    VerticalLayout chartLayout;
    ComboBox<String> queries;

    public CommonQueriesView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(0, 0.65f);
        grid.setColumnExpandRatio(1, 0.35f);

        chartLayout = new VerticalLayout();
        chartLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        grid.addComponent(chartLayout);

        VerticalLayout formLayout = createFormPanel();
        grid.addComponent(formLayout);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createFormPanel() {

        val vl = new VerticalLayout();
        vl.setSpacing(false);
        vl.setMargin(false);

        val form = new VerticalLayout();
        form.setSizeFull();
        form.setMargin(false);
        form.setMargin(new MarginInfo(true,false,false,false));
        form.setSpacing(false);

        val queriesList = Arrays.asList(
                "Capabilities per type",
                "Resources per capability",
                "Resources per capability type"
        );

        queries = new ComboBox<>("Queries");
        queries.setItems(queriesList);
        queries.setSizeFull();
        queries.addValueChangeListener(e -> {
            form.removeAllComponents();
            form.addComponent(createFormQuery(queries.getValue()));
        });

        vl.addComponent(queries);
        vl.addComponent(form);

        return vl;
    }

    private VerticalLayout createFormQuery(String query) {

        if (query.equals("Capabilities per type")) {
            return createCapabilitiesPerTypeForm();
        }
        else if (query.equals("Resources per capability")) {
            return createResourcesPerCapabilityForm();
        }
        else if (query.equals("Resources per capability type")) {
            return createResourcesPerCapabilityTypeForm();
        }

        return null;
    }

    private VerticalLayout createCapabilitiesPerTypeForm() {
        val vl = new VerticalLayout();
        vl.setMargin(false);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotCapabilitiesPerTypeChart());
        vl.addComponent(b);
        return vl;
    }

    private VerticalLayout createResourcesPerCapabilityTypeForm() {
        val vl = new VerticalLayout();
        vl.setMargin(false);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotResourcesPerCapabilityTypeChart());
        vl.addComponent(b);
        return vl;
    }

    private VerticalLayout createResourcesPerCapabilityForm() {
        val vl = new VerticalLayout();
        //vl.setSpacing(false);
        vl.setMargin(false);

        val capabilities = interSCityService.getAllCapabilities(Option.none()).stream()
                .map(cap -> cap.getName())
                .sorted();

        ListSelect<String> select = new ListSelect<>("Capabilities");
        select.setSizeFull();
        select.setItems(capabilities);
        vl.addComponent(select);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotResourcesPerCapabilityChart(select.getSelectedItems()));
        vl.addComponent(b);
        return vl;
    }

    private void plotResourcesPerCapabilityChart(Set<String> capabilities) {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        chartLayout.addComponent(spinner);

        val chart = new VerticalBarChart();
        chart.setTitle(queries.getValue().toUpperCase());

        capabilities.stream().forEach(cap -> {
            val request = new SearchResourcesRequest();
            request.setCapability(cap);
            val resources = interSCityService.searchResources(request);
            resources.stream().forEach(res -> chart.addData("Resources", cap, (double) resources.size()));
        });

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotResourcesPerCapabilityTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        chartLayout.addComponent(spinner);

        int sensors = interSCityService.getAllResourcesWithSensorCapabilities().size();
        int actuators = interSCityService.getAllResourcesWithActuatorCapabilities().size();

        PieChart pieChart = new PieChart();
        pieChart.setTitle(queries.getValue().toUpperCase());
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        chartLayout.removeAllComponents();
        chartLayout.addComponent(pieChart.getChart());
    }

    private void plotCapabilitiesPerTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        chartLayout.addComponent(spinner);

        int sensors = interSCityService.getAllCapabilities(Option.of("sensor")).size();
        int actuators = interSCityService.getAllCapabilities(Option.of("actuator")).size();

        PieChart pieChart = new PieChart();
        pieChart.setTitle(queries.getValue().toUpperCase());
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        chartLayout.removeAllComponents();
        chartLayout.addComponent(pieChart.getChart());

    }


}

