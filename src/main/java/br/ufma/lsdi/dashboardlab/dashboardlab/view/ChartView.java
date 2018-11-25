package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.contextdata.GetContextDataRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChartView extends VerticalLayout {

    private final InterSCityService interSCityService;

    public ChartView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(0, 0.65f);
        grid.setColumnExpandRatio(1, 0.35f);

        VerticalLayout chartLayout = new VerticalLayout();
        grid.addComponent(chartLayout);

        VerticalLayout formLayout = createFormPanel(chartLayout);
        grid.addComponent(formLayout);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createFormPanel(VerticalLayout chartLayout) {

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(false);
        //vl.setSpacing(false);

        Label label = new Label("Chart Options");
        label.addStyleName("h2");

        vl.addComponent(label);

        val chartTypesList =
                Arrays.asList("Vertical Bar", "Horizontal Bar",
                        "Line", "Scatter Line", "Stepped Line",
                        "Pie", "Donut", "Timeline", "Bubble");

        ComboBox<String> chartTypesCombobox = new ComboBox<>();
        chartTypesCombobox.setSizeFull();
        chartTypesCombobox.setItems(chartTypesList);

        Button button = new Button("Plot Chart");
        button.addClickListener(e -> plotChart(chartLayout, chartTypesCombobox.getValue()));

        vl.addComponent(chartTypesCombobox);
        vl.addComponent(button);

        if (1==1) return vl;

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();

        VerticalLayout tab1 = new VerticalLayout();
        tab1.setSpacing(true);
        tab1.setMargin(true);

        VerticalLayout tab2 = new VerticalLayout();
        tab2.setSpacing(true);
        tab2.setMargin(true);

        VerticalLayout tab3 = new VerticalLayout();
        tab3.setSpacing(true);
        tab3.setMargin(true);

        tabSheet.addTab(tab1).setCaption("1 - Resources");
        tabSheet.addTab(tab2).setCaption("2 - Capabilities");
        tabSheet.addTab(tab3).setCaption("3 - Context Data");

        vl.addComponent(tabSheet);

        ListSelect<String> capabilityListSelect = new ListSelect<>("Capabilities");
        capabilityListSelect .setSizeFull();

        List<Resource> resources = interSCityService.getAllResources().stream().sorted(Comparator.comparing(Resource::getDescription)).collect(Collectors.toList());

        ListSelect<Resource> resourceListSelect = new ListSelect<>("Resources");
        resourceListSelect.setSizeFull();
        resourceListSelect.setItems(resources);
        resourceListSelect.addValueChangeListener(e -> {
            List<List<String>> list = resourceListSelect.getSelectedItems().stream()
                    .map(item -> item.getCapabilities()).collect(Collectors.toList());
            Set<String> capabilitiesSet = list.stream().flatMap(x -> x.stream()).collect(Collectors.toSet());
            List<String> capabilities = new ArrayList<>(capabilitiesSet);
            capabilities = capabilities.stream().sorted().collect(Collectors.toList());
            capabilityListSelect.setItems(capabilities);
        });

        Button button1next = new Button("Next >>");
        button1next.addClickListener(e -> tabSheet.setSelectedTab(tab2));

        tab1.addComponent(resourceListSelect);
        tab1.addComponent(button1next);


        Button button2previous = new Button("<< Previous");
        button2previous.addClickListener(e -> tabSheet.setSelectedTab(tab1));
        Button button2next = new Button("Next >>");
        button2next.addClickListener(e -> tabSheet.setSelectedTab(tab3));

        tab2.addComponent(capabilityListSelect);
        tab2.addComponent(new HorizontalLayout(button2previous, button2next));

        capabilityListSelect.addValueChangeListener(e -> {
            System.out.println(capabilityListSelect.getSelectedItems());
        });

        Button button3previous = new Button("<< Previous");
        button3previous.addClickListener(e -> tabSheet.setSelectedTab(tab2));
        Button button3next = new Button("Plot Chart >>");
        //button3next.addClickListener(e -> plotChart());


        Set<String> items = new HashSet<>();
        ListSelect<String> contextDataNameListSelect = new ListSelect<>();
        contextDataNameListSelect.setSizeFull();
        contextDataNameListSelect.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.DELETE, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                items.removeAll(contextDataNameListSelect.getSelectedItems());
                contextDataNameListSelect.setItems(items);
            }
        });

        TextField textField = new TextField("Context Data Name");
        textField.setSizeFull();
        textField.setPlaceholder("Enter context data name");
        textField.addShortcutListener(new ShortcutListener("", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                items.add(textField.getValue());
                textField.clear();
                contextDataNameListSelect.setItems(items);
            }
        });



        tab3.addComponent(textField);
        tab3.addComponent(contextDataNameListSelect);
        tab3.addComponent(new HorizontalLayout(button3previous, button3next));

        return vl;
    }

    private void plotChart(VerticalLayout vl, String chartType) {

        if (chartType.equals("Vertical Bar")) {
            val chart = new VerticalBarChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Horizontal Bar")) {
            val chart = new HorizontalBarChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Line")) {
            val chart = new LineChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Scatter Line")) {
            val chart = new ScatterLineChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Stepped Line")) {
            val chart = new SteppedLineChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Pie")) {
            val chart = new PieChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Donut")) {
            val chart = new DonutChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Timeline")) {
            val chart = new TimeLineChart();
            vl.addComponent(chart.getChart());
        }
        else if (chartType.equals("Bubble")) {
            val chart = new BubbleChart();
            vl.addComponent(chart.getChart());
        }

    }

}
