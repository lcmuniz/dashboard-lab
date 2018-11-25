package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import io.vavr.control.Option;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CommonQueriesView extends VerticalLayout {

    private final InterSCityService interSCityService;

    Gson gson = AppGson.get();

    TextField uuidTextField;
    TextField capabilityTextField;
    TextField contextDataXTextField;
    TextField contextDataYTextField;

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
/*
        uuidTextField = new TextField("UUID");
        capabilityTextField = new TextField("Capability");
        contextDataXTextField = new TextField("Context Data (X)");
        contextDataYTextField = new TextField("Context Data (Y)");
*/

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

        val request = new SearchResourcesRequest();
        List<Double> qr = new ArrayList<>();
        for (String capability : capabilities) {
            request.setCapability(capability);
            val resources = interSCityService.searchResources(request);
            qr.add((double)resources.size());
        }

        chartLayout.removeAllComponents();
        val chart = new VerticalBarChart();

        chart.setTitle(queries.getValue());
        String[] labels = capabilities.stream().toArray(String[]::new);
        chart.setLabels(labels);
        chart.setYLabel("Resources");
        chart.setDatasetLabel("Capabilities");
        chart.setData(qr);
        chartLayout.addComponent(chart.getChart());
    }

    private void plotResourcesPerCapabilityTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        chartLayout.addComponent(spinner);

        int sensors = interSCityService.getAllResourcesWithSensorCapabilities().size();
        int actuators = interSCityService.getAllResourcesWithActuatorCapabilities().size();

        List<Double> qr = new ArrayList<>();
        qr.add((double) sensors);
        qr.add((double) actuators);

        System.out.println(sensors);
        System.out.println(actuators);

        chartLayout.removeAllComponents();
        val chart = new PieChart();

        chart.setTitle(queries.getValue());
        String[] labels = {"Sensors", "Actuators"};
        chart.setLabels(labels);
        chart.setData(qr);

        chartLayout.addComponent(chart.getChart());
    }

    private void plotCapabilitiesPerTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        chartLayout.addComponent(spinner);

        int sensors = interSCityService.getAllCapabilities(Option.of("sensor")).size();
        int actuators = interSCityService.getAllCapabilities(Option.of("actuator")).size();

        List<Double> qr = new ArrayList<>();
        qr.add((double) sensors);
        qr.add((double) actuators);

        System.out.println(sensors);
        System.out.println(actuators);

        chartLayout.removeAllComponents();
        val chart = new PieChart();

        chart.setTitle(queries.getValue().toUpperCase());
        String[] labels = {"Sensors", "Actuators"};
        chart.setLabels(labels);
        chart.setData(qr);

        chartLayout.addComponent(chart.getChart());
    }


}

