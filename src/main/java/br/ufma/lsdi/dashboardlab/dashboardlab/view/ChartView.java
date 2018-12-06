package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.SelectWithTextField;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataResponse;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetDataContextResource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Data;
import lombok.val;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChartView extends VerticalLayout {

    private final InterSCityService interSCityService;

    VerticalLayout chartLayout;
    VerticalLayout formLayout;

    SelectWithTextField resourcesList;
    SelectWithTextField capabilitiesList;
    SelectWithTextField matchersList;
    DateTimeField startDate;
    DateTimeField endDate;

    ComboBox<String> chartTypesCombobox;
    private TextField title;
    private RadioButtonGroup<String> groupType;
    private TextField x;
    private TextField y;
    private TextField group;
    private TextField value;
    private HorizontalLayout xy;
    private HorizontalLayout gv;
    private TextField group_sl;
    private TextField x_sl;
    private TextField y_sl;
    private HorizontalLayout line_sl;
    private TextField group_bubble;
    private TextField size_bubble;
    private TextField x_bubble;
    private TextField y_bubble;
    private HorizontalLayout line_bubble;

    public ChartView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(0, 0.65f);
        grid.setColumnExpandRatio(1, 0.35f);

        chartLayout = new VerticalLayout();
        grid.addComponent(chartLayout);

        formLayout = createFormPanel();
        grid.addComponent(formLayout);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createFormPanel() {

        VerticalLayout vl = new VerticalLayout();
        vl.setMargin(false);

        resourcesList = new SelectWithTextField("Resources");
        capabilitiesList = new SelectWithTextField("Capabilities");
        matchersList = new SelectWithTextField("Matchers");
        startDate = new DateTimeField("Start Date");
        endDate = new DateTimeField("End Date");

        startDate.setValue(LocalDateTime.of(2018, 01, 01, 00, 00));
        endDate.setValue(LocalDateTime.of(2018, 12, 31, 00, 00));

        configDeleteListener(resourcesList, capabilitiesList, matchersList);

        TabSheet tabs = new TabSheet();
        tabs.setTabsVisible(false);
        val step1Tab = new VerticalLayout();
        val step2Tab = new VerticalLayout();
        val step3Tab = new VerticalLayout();
        val step4Tab = new VerticalLayout();
        val step5Tab = new VerticalLayout();

        val step1nextButton = new Button("Next >>");
        step1nextButton.addClickListener(e -> {
            tabs.setSelectedTab(step2Tab);
            configDeleteListener(capabilitiesList,resourcesList, matchersList);
        });
        val step2previousButton = new Button("<< Previous");
        val step2nextButton = new Button("Next >>");
        step2previousButton.addClickListener(e -> {
            tabs.setSelectedTab(step1Tab);
            configDeleteListener(resourcesList, capabilitiesList, matchersList);
        });
        step2nextButton.addClickListener(e -> {
            tabs.setSelectedTab(step3Tab);
            configDeleteListener(matchersList, resourcesList, capabilitiesList);
        });
        val step3previousButton = new Button("<< Previous");
        val step3nextButton = new Button("Next >>");
        step3previousButton.addClickListener(e -> {
            tabs.setSelectedTab(step2Tab);
            configDeleteListener(capabilitiesList, matchersList, resourcesList);
        });
        step3nextButton.addClickListener(e -> {
            tabs.setSelectedTab(step4Tab);
            configDeleteListener(resourcesList, capabilitiesList, matchersList);
        });
        val step4previousButton = new Button("<< Previous");
        val step4nextButton = new Button("Next >>");

        step4previousButton.addClickListener(e -> {
            tabs.setSelectedTab(step3Tab);
            configDeleteListener(matchersList, resourcesList, capabilitiesList);
        });

        step4nextButton.addClickListener(e -> {
            tabs.setSelectedTab(step5Tab);
        });

        step1Tab.addComponent(new Label("Search Resources"));
        step1Tab.addComponent(resourcesList);
        step1Tab.addComponent(step1nextButton);

        step2Tab.addComponent(new Label("Search Capabilities"));
        step2Tab.addComponent(capabilitiesList);
        step2Tab.addComponent(new HorizontalLayout(step2previousButton, step2nextButton));

        step3Tab.addComponent(new Label("Include Matchers"));
        step3Tab.addComponent(matchersList);
        step3Tab.addComponent(new HorizontalLayout(step3previousButton, step3nextButton));

        step4Tab.addComponent(new Label("Specify Date Range"));
        step4Tab.addComponent(startDate);
        step4Tab.addComponent(endDate);
        step4Tab.addComponent(new HorizontalLayout(step4previousButton, step4nextButton));

        val chartTypesList =
                Arrays.asList("Vertical Bar", "Horizontal Bar",
                        "Line", "Scatter Line", "Stepped Line",
                        "Pie", "Donut", "Time Line", "Bubble");

        chartTypesCombobox = new ComboBox<>("Chart type");
        chartTypesCombobox.setSizeFull();
        chartTypesCombobox.setItems(chartTypesList);
        chartTypesCombobox.setValue("Vertical Bar");
        chartTypesCombobox.addValueChangeListener(e ->
                onChangeChartType(chartTypesCombobox.getValue()));

        title = new TextField("Title");
        title.setSizeFull();

        x = new TextField("X");
        x.setSizeFull();

        y = new TextField("Y");
        y.setSizeFull();

        xy = new HorizontalLayout();
        xy.setSizeFull();
        xy.addComponents(x, y);

        group = new TextField("Group");
        group.setSizeFull();

        value = new TextField("Value");
        value.setSizeFull();

        gv = new HorizontalLayout();
        gv.setSizeFull();
        gv.addComponents(group, value);
        gv.setVisible(false);

        group_sl = new TextField("Group");
        group_sl.setSizeFull();

        x_sl = new TextField("X");
        x_sl.setSizeFull();

        y_sl = new TextField("Y");
        y_sl.setSizeFull();

        line_sl = new HorizontalLayout();
        line_sl.setSizeFull();
        line_sl.addComponents(group_sl, x_sl, y_sl);
        line_sl.setVisible(false);

        group_bubble = new TextField("Group");
        group_bubble.setSizeFull();

        size_bubble = new TextField("Size");
        size_bubble.setSizeFull();

        x_bubble = new TextField("X");
        x_bubble.setSizeFull();

        y_bubble = new TextField("Y");
        y_bubble.setSizeFull();

        line_bubble = new HorizontalLayout();
        line_bubble.setSizeFull();
        line_bubble.addComponents(group_bubble, size_bubble, x_bubble, y_bubble);
        line_bubble.setVisible(false);

        groupType = new RadioButtonGroup<>();
        groupType.setItems("Sum", "Count", "Avg", "Min", "Max");
        groupType.setValue("Sum");
        groupType.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        Button plotButton = new Button("Plot Chart");
        plotButton.addClickListener(e -> plotChart(chartTypesCombobox));

        step5Tab.addComponent(chartTypesCombobox);
        step5Tab.addComponent(new Label("Plot Chart"));
        step5Tab.addComponent(title);
        step5Tab.addComponent(xy);
        step5Tab.addComponent(gv);
        step5Tab.addComponent(line_sl);
        step5Tab.addComponent(line_bubble);
        step5Tab.addComponent(groupType);

        val step5previousButton = new Button("<< Previous");

        step5previousButton.addClickListener(e -> {
            tabs.setSelectedTab(step4Tab);
        });

        step5Tab.addComponent(new HorizontalLayout(step5previousButton, plotButton));

        tabs.addTab(step1Tab);
        tabs.addTab(step2Tab);
        tabs.addTab(step3Tab);
        tabs.addTab(step4Tab);
        tabs.addTab(step5Tab);

        Label label = new Label("Chart Options");
        label.addStyleName("h2");

        vl.addComponent(label);
        vl.addComponent(tabs);

        if (1==1) return vl;

        TextField filter = new TextField("Filter");
        filter.setSizeFull();
        TextField groupSL = new TextField("Group");
        groupSL.setSizeFull();
        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.addComponents(filter, groupSL);
        filterLayout.setSizeFull();
        filterLayout.setVisible(false);

        TextField xAxis = new TextField("X");
        xAxis.setSizeFull();
        TextField yAxis = new TextField("Y");
        yAxis.setSizeFull();
        HorizontalLayout axes = new HorizontalLayout();
        axes.setSizeFull();
        axes.addComponents(xAxis, yAxis);
        axes.setVisible(false);


        DateTimeField start = new DateTimeField("Start Range");
        start.setSizeFull();

        DateTimeField end = new DateTimeField("End Range");
        end.setSizeFull();

        start.setValue(LocalDateTime.of(2018, 01, 01, 00, 00));
        end.setValue(LocalDateTime.of(2018, 12, 31, 00, 00));

        HorizontalLayout dates =  new HorizontalLayout();
        dates.addComponents(start, end);
        dates.setSizeFull();

        vl.addComponent(label);
        vl.addComponent(chartTypesCombobox);
        vl.addComponent(title);
        vl.addComponent(y);
        vl.addComponent(filterLayout);
        vl.addComponent(x);
        vl.addComponent(axes);
        vl.addComponent(groupType);
        vl.addComponent(dates);
        vl.addComponent(plotButton);

        return vl;
    }

    private void configDeleteListener(SelectWithTextField first, SelectWithTextField second, SelectWithTextField third) {
        third.removeListShortcut();
        second.removeListShortcut();
        first.addListShorcut();
    }

    private void plotChart(ComboBox<String> chartTypesCombobox) {

        List<GetDataContextResource> resources = searchResources();

        if (chartTypesCombobox.getValue().equals("Vertical Bar")) {
            plotVerticalBarChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Horizontal Bar")) {
            plotHorizontalBarChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Pie")) {
            plotPieChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Donut")) {
            plotDonutChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Line")) {
            plotLineChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Stepped Line")) {
            plotSteppedLineChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Scatter Line")) {
            plotScatterLineChart(resources);
        }
        else if (chartTypesCombobox.getValue().equals("Bubble")) {
            plotBubbleChart(resources);
        }
    }

    private List<GetDataContextResource> searchResources() {
        val request = new GetContextDataRequest();
        val uuids = new ArrayList<String>();
        for (String resourceName : resourcesList.getItems()) {
            val resource = interSCityService.getResourceByDescription(resourceName);
            if (resource != null) uuids.add(resource.getUuid());
        }
        request.setUuids(uuids);
        request.setCapabilities(capabilitiesList.getItems());

        Map<String, Object> map1 = new HashMap<>();
        matchersList.getItems().forEach(m -> {
            val match = m.split("=");
            map1.put(match[0], match[1]);
        });
        request.setMatchers(map1);
        request.setStartDate(startDate.getValue());
        request.setEndDate(endDate.getValue());

        GetContextDataResponse response = interSCityService.getLastContextData(request);

        return response.getResources();
    }

    private void plotBubbleChart(List<GetDataContextResource> resources) {
        BubbleChart chart = new BubbleChart();
        chart.setTitle(title.getValue());
        chart.setGroupType(groupType.getValue());
        chart.setX(x_bubble.getValue());
        chart.setY(y_bubble.getValue());

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    if (map.get(x_bubble.getValue()) != null && map.get(y_bubble.getValue()) != null && map.get(size_bubble.getValue()) != null) {
                        chart.addData((String) map.get(group_bubble.getValue()), (Double) map.get(x_bubble.getValue()), (Double) map.get(y_bubble.getValue()), (Double) map.get(size_bubble.getValue()));
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());

    }

    private void plotHorizontalBarChart(List<GetDataContextResource> resources) {
        HorizontalBarChart chart = new HorizontalBarChart();
        plotBarChart(chart, resources);
    }

    private void plotVerticalBarChart(List<GetDataContextResource> resources) {
        VerticalBarChart chart = new VerticalBarChart();
        plotBarChart(chart, resources);
    }

    private void plotScatterLineChart(List<GetDataContextResource> resources) {

        ScatterLineChart chart = new ScatterLineChart();
        chart.setTitle(title.getValue());
        chart.setX(x_sl.getValue());
        chart.setY(y_sl.getValue());

        List<String> xys = Arrays.asList(x_sl.getValue(), y_sl.getValue());

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    for(String xy : xys) {
                        if (map.get(xy) != null) {
                            chart.addData(map.get(group_sl.getValue()), xy, (Double) map.get(xy));
                        }
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotSteppedLineChart(List<GetDataContextResource> resources) {

        SteppedLineChart chart = new SteppedLineChart();
        chart.setTitle(title.getValue().toUpperCase());
        chart.setGroupType(groupType.getValue());

        String[] ys = y.getValue().replace(" ", "").split(",");

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    for(String y : ys) {
                        if (map.get(y) != null) {
                            chart.addData((String) map.get(x.getValue()), y, (Double) map.get(y));
                        }
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotLineChart(List<GetDataContextResource> resources) {

        LineChart chart = new LineChart();
        chart.setTitle(title.getValue().toUpperCase());
        chart.setGroupType(groupType.getValue());

        String[] ys = y.getValue().replace(" ", "").split(",");

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    for(String y : ys) {
                        if (map.get(y) != null) {
                            chart.addData((String) map.get(x.getValue()), y, (Double) map.get(y));
                        }
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotBarChart(IBarChart chart, List<GetDataContextResource> resources) {

        chart.setTitle(title.getValue().toUpperCase());
        chart.setGroupType(groupType.getValue());

        String[] ys = y.getValue().replace(" ", "").split(",");

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    for(String y : ys) {
                        if (map.get(y) != null) {
                            chart.addData((String) map.get(x.getValue()), y, (Double) map.get(y));
                        }
                    }

                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotPieChart(List<GetDataContextResource> resources) {
        PieChart chart = new PieChart();

        chart.setTitle(title.getValue());
        chart.setGroupType(groupType.getValue());

        String v = value.getValue();

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    if (map.get(v) != null) {
                        chart.addData((String) map.get(group.getValue()), (Double) map.get(v));
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotDonutChart(List<GetDataContextResource> resources) {

        DonutChart chart = new DonutChart();
        chart.setTitle(title.getValue());
        chart.setGroupType(groupType.getValue());

        String[] vs = value.getValue().replace(" ", "").split(",");

        for (GetDataContextResource resource : resources) {
            Map<String, List<Map<String, Object>>> capabilitiesMap = resource.getCapabilities();

            Set<String> capabilitiesList = capabilitiesMap.keySet();
            for (String capabilityName : capabilitiesList) {

                List<Map<String, Object>> mapList = capabilitiesMap.get(capabilityName);

                mapList.stream().forEach(map -> {
                    for(String v : vs) {
                        if (map.get(v) != null) {
                            chart.addData((String) map.get(group.getValue()), v, (Double) map.get(v));
                        }
                    }
                });

            }

        }

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());

    }

    private void onChangeChartType(String chartType) {
        xy.setVisible(true);
        gv.setVisible(false);
        line_sl.setVisible(false);
        line_bubble.setVisible(false);
        if (chartType.equals("Vertical Bar")) {
        }
        else if (chartType.equals("Horizontal Bar")) {
        }
        else if (chartType.equals("Line")) {
        }
        else if (chartType.equals("Scatter Line")) {
            xy.setVisible(false);
            gv.setVisible(false);
            line_sl.setVisible(true);
            line_bubble.setVisible(false);
        }
        else if (chartType.equals("Stepped Line")) {
        }
        else if (chartType.equals("Pie")) {
            xy.setVisible(false);
            gv.setVisible(true);
            line_sl.setVisible(false);
            line_bubble.setVisible(false);
        }
        else if (chartType.equals("Donut")) {
            xy.setVisible(false);
            gv.setVisible(true);
            line_sl.setVisible(false);
            line_bubble.setVisible(false);
        }
//        else if (chartType.equals("Time Line")) {
//
//        }
        else if (chartType.equals("Bubble")) {
            xy.setVisible(false);
            gv.setVisible(false);
            line_sl.setVisible(false);
            line_bubble.setVisible(true);
        }

    }

    private void x() {

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

        //vl.addComponent(tabSheet);

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

        TextField contextDataNameTextField = new TextField("Context Data Property: ");
        contextDataNameTextField.setSizeFull();
        RadioButtonGroup<String> actionsRadio = new RadioButtonGroup<>();
        actionsRadio.setItems("Sum", "Count", "Avg");
        actionsRadio.setValue("Sum");
        actionsRadio.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        Set<String> items = new HashSet<>();
        ListSelect<String> contextDataNameListSelect = new ListSelect<>();

        TextField titleTextField = new TextField("Chart Title");
        titleTextField.setSizeFull();

        val chartTypesList =
                Arrays.asList("Vertical Bar", "Horizontal Bar",
                        "Line", "Scatter Line", "Stepped Line",
                        "Pie", "Donut", "Timeline", "Bubble");

        ComboBox<String> chartTypesCombobox = new ComboBox<>();
        chartTypesCombobox.setSizeFull();
        chartTypesCombobox.setItems(chartTypesList);
        chartTypesCombobox.setValue("Vertical Bar");

        Button button3previous = new Button("<< Previous");
        button3previous.addClickListener(e -> tabSheet.setSelectedTab(tab2));
        Button button3next = new Button("Plot Chart >>");
        button3next.addClickListener(e ->
                plotChart1(resourceListSelect,
                        capabilityListSelect,
                        contextDataNameTextField.getValue(),
                        actionsRadio.getValue(),
                        titleTextField.getValue(),
                        chartTypesCombobox.getValue()));


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


        tab3.addComponent(contextDataNameTextField);
        tab3.addComponent(actionsRadio);
        tab3.addComponent(titleTextField);
        tab3.addComponent(chartTypesCombobox);

        tab3.addComponent(new HorizontalLayout(button3previous, button3next));


    }

    @Data
    class PlotData {
        String resource;
        String capability;
        String key;
        Object value;
        LocalDateTime date;
        public Integer getIntValue() {
            return (Integer) value;
        }
        public Double getDoubleValue() {
            return (Double) value;
        }
    }

    private void plotChart1(ListSelect<Resource> resourceListSelect,
                           ListSelect<String> capabilityListSelect,
                           String property, String action, String title,
                           String chartType) {

        if (chartType.equals("Vertical Bar")) {
            plotChartV(resourceListSelect,
                    capabilityListSelect,
                    property, action, title);
        }
        else if (chartType.equals("Horizontal Bar")) {
            plotChartH(resourceListSelect,
                    capabilityListSelect,
                    property, action, title);
        }
        else if (chartType.equals("Line")) {
            val chart = new LineChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }
        else if (chartType.equals("Scatter Line")) {
            val chart = new ScatterLineChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }
        else if (chartType.equals("Stepped Line")) {
            val chart = new SteppedLineChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }
        else if (chartType.equals("Pie")) {
            val chart = new PieChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }
        else if (chartType.equals("Donut")) {
            val chart = new DonutChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }
        else if (chartType.equals("Timeline")) {
            plotChartT(resourceListSelect,
                    capabilityListSelect,
                    property, action, title);
        }
        else if (chartType.equals("Bubble")) {
            val chart = new BubbleChart();
            chartLayout.removeAllComponents();
            chartLayout.addComponent(chart.getChart());
        }

    }

    private void plotChartV(ListSelect<Resource> resourceListSelect,
                           ListSelect<String> capabilityListSelect,
                           String property, String action, String title) {

        Map<String, String> map = new HashMap<>();
        resourceListSelect.getSelectedItems().stream().forEach(r ->
                map.put(r.getUuid(), r.getDescription())
        );
        List<String> resourceNames = new ArrayList<>(map.values());
        List<String> uuids = resourceListSelect.getSelectedItems().stream().map(r -> r.getUuid()).collect(Collectors.toList());
        List<String> capabilities = new ArrayList<>(capabilityListSelect.getSelectedItems());
        List<String> values = new ArrayList<>();
        values.add(property);

        GetContextDataRequest request = new GetContextDataRequest();
        request.setUuids(uuids);
        request.setCapabilities(capabilities);

        GetContextDataResponse response = interSCityService.getContextData(request);

        List<PlotData> plotDatas = new ArrayList<>();
        for (GetDataContextResource resource : response.getResources()){
            for (String key : resource.getCapabilities().keySet()) {
                List<Map<String, Object>> contextDatas = resource.getCapabilities().get(key);
                for (Map<String, Object> contextData : contextDatas) {
                    for(String dataKey : contextData.keySet()) {
                        if (values.contains(dataKey)) {
                            PlotData data = new PlotData();
                            data.setResource(map.get(resource.getUuid()));
                            data.setCapability(key);
                            data.setKey(dataKey);
                            data.setValue(contextData.get(dataKey));
                            plotDatas.add(data);
                        }
                    }
                }
            }
        }

        val chart = new VerticalBarChart();


        if (action.equals("Sum")) {
            Map<String, Map<String, Map<String, Double>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.summingDouble(PlotData::getDoubleValue)))));
            createDatasetsDouble(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Count")) {
            Map<String, Map<String, Map<String, Long>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.counting()))));
            createDatasetsLong(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Avg")) {
            Map<String, Map<String, Map<String, Double>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.averagingDouble(PlotData::getDoubleValue)))));
            createDatasetsDouble(resourceNames, capabilities, values, result, chart);
        }

        String[] labels = resourceNames.toArray(new String[resourceNames.size()]);

        //chart.setLabels(labels);
        chart.setTitle(title);
        //chart.setYLabel("Value");

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void createDatasetsDouble(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Double>>> result, IBarChart chart) {
        for (String capabilityName: capabilities) {
            Map<String, Map<String, Double>> resources = result.get(capabilityName);
            List<Double> data = new ArrayList<>();
            for (String resourceName : resourceNames) {
                Map<String, Double> valuesList = resources.get(resourceName);
                for (String val : values) {
                    Double value = valuesList.get(val);
                    data.add(value);
                }
            }
            //val dataset1 = new VerticalBarChart.DatasetX();
            //dataset1.setLabel(capabilityName);
            //dataset1.setData(data);
            //chart.addDatasetX(dataset1);
        }
    }

    private void createDatasetsDoubleT(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Map<LocalDateTime, Double>>>> result, TimeLineChart chart) {
        for (String capabilityName: capabilities) {
            Map<String, Map<String, Map<LocalDateTime, Double>>> resources = result.get(capabilityName);
            List<Double> data = new ArrayList<>();
            List<LocalDateTime> dates = new ArrayList<>();
            for (String resourceName : resourceNames) {
                Map<String, Map<LocalDateTime, Double>> dateList = resources.get(resourceName);
                for(String date : dateList.keySet()) {
                    Map<LocalDateTime, Double> valuesList = dateList.get((date));
                    for (LocalDateTime dt : valuesList.keySet()) {
                        dates.add(dt);
                        data.add(valuesList.get(dt));
                    }
                }
            }
            //val dataset1 = new VerticalBarChart.DatasetX();
            //dataset1.setLabel(capabilityName);
            //dataset1.setData(data);
            //dataset1.setDate(dates);
            //chart.addDatasetX(dataset1);
        }
    }

    private void createDatasetsLongT(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Map<LocalDateTime, Long>>>> result, TimeLineChart chart) {
        for (String capabilityName: capabilities) {
            Map<String, Map<String, Map<LocalDateTime, Long>>> resources = result.get(capabilityName);
            List<Double> data = new ArrayList<>();
            List<LocalDateTime> dates = new ArrayList<>();
            for (String resourceName : resourceNames) {
                Map<String, Map<LocalDateTime, Long>> dateList = resources.get(resourceName);
                for(String date : dateList.keySet()) {
                    Map<LocalDateTime, Long> valuesList = dateList.get((date));
                    for (String val : values) {
                        Long value = valuesList.get(val);
                        data.add((double) value);
                        //dates//.add(date)
                    }
                }
            }
            //val dataset1 = new VerticalBarChart.DatasetX();
            //dataset1.setLabel(capabilityName);
            //dataset1.setData(data);
            //dataset1.setDate(dates);
            //chart.addDatasetX(dataset1);
        }
    }

    private void createDatasetsLong(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Long>>> result, IBarChart chart) {
        for (String capabilityName: capabilities) {
            Map<String, Map<String, Long>> resources = result.get(capabilityName);
            List<Double> data = new ArrayList<>();
            for (String resourceName : resourceNames) {
                Map<String, Long> valuesList = resources.get(resourceName);
                for (String val : values) {
                    Double value = (double) valuesList.get(val);
                    data.add(value);
                }
            }
            //val dataset1 = new VerticalBarChart.DatasetX();
            //dataset1.setLabel(capabilityName);
            //dataset1.setData(data);
            //chart.addDatasetX(dataset1);
        }
    }



    private void createVerticalBarForm(VerticalLayout vl) {

        TextField labels = new TextField("Labels");
        labels.setSizeFull();

        Button button = new Button("Plot Chart");
        button.addClickListener(e -> createVerticalBarChart(labels.getValue()));

        vl.addComponent(labels);
        vl.addComponent(button);
    }

    private void createVerticalBarChart(String labels) {

        GetContextDataRequest request = new GetContextDataRequest();
        request.getCapabilities().add("crime_doloso_lc");
        GetContextDataResponse response = interSCityService.getContextData(request);

        for (GetDataContextResource resource : response.getResources()){
            for (String key : resource.getCapabilities().keySet()) {
                List<Map<String, Object>> value = resource  .getCapabilities().get(key);
            }
        }

        val chart = new VerticalBarChart();

        //val dataset1 = new VerticalBarChart.DatasetX();
        //dataset1.setLabel("data1");
        //dataset1.setData(Arrays.asList(3.0,3.0,3.0,0.0));

        //val dataset2 = new VerticalBarChart.DatasetX();
        //dataset2.setLabel("data2");
        //dataset2.setData(Arrays.asList(3.0,2.0,2.0));

        //chart.addDatasetX(dataset1);
        //chart.addDatasetX(dataset2);

        //chart.setLabels(Arrays.asList("Teste1","Teste2","Teste3"));
        chart.setTitle("Okokok");
        //chart.setYLabel("Dasdasd");

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

    private void plotChartH(ListSelect<Resource> resourceListSelect,
                            ListSelect<String> capabilityListSelect,
                            String property, String action, String title) {

        Map<String, String> map = new HashMap<>();
        resourceListSelect.getSelectedItems().stream().forEach(r ->
                map.put(r.getUuid(), r.getDescription())
        );
        List<String> resourceNames = new ArrayList<>(map.values());
        List<String> uuids = resourceListSelect.getSelectedItems().stream().map(r -> r.getUuid()).collect(Collectors.toList());
        List<String> capabilities = new ArrayList<>(capabilityListSelect.getSelectedItems());
        List<String> values = new ArrayList<>();
        values.add(property);

        GetContextDataRequest request = new GetContextDataRequest();
        request.setUuids(uuids);
        request.setCapabilities(capabilities);

        GetContextDataResponse response = interSCityService.getContextData(request);

        List<PlotData> plotDatas = new ArrayList<>();
        for (GetDataContextResource resource : response.getResources()){
            for (String key : resource.getCapabilities().keySet()) {
                List<Map<String, Object>> contextDatas = resource.getCapabilities().get(key);
                for (Map<String, Object> contextData : contextDatas) {
                    for(String dataKey : contextData.keySet()) {
                        if (values.contains(dataKey)) {
                            PlotData data = new PlotData();
                            data.setResource(map.get(resource.getUuid()));
                            data.setCapability(key);
                            data.setKey(dataKey);
                            data.setValue(contextData.get(dataKey));
                            plotDatas.add(data);
                        }
                    }
                }
            }
        }

        //val chart = new HorizontalBarChart1();


        if (action.equals("Sum")) {
            Map<String, Map<String, Map<String, Double>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.summingDouble(PlotData::getDoubleValue)))));
            //createDatasetsDouble(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Count")) {
            Map<String, Map<String, Map<String, Long>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.counting()))));
            //createDatasetsLong(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Avg")) {
            Map<String, Map<String, Map<String, Double>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.averagingDouble(PlotData::getDoubleValue)))));
            //createDatasetsDouble(resourceNames, capabilities, values, result, chart);
        }

        String[] labels = resourceNames.toArray(new String[resourceNames.size()]);

        //chart.setLabels(labels);
        //chart.setTitle(title);
        //chart.setYLabel("Value");

        chartLayout.removeAllComponents();
        //chartLayout.addComponent(chart.getChart());
    }

    private void plotChartT(ListSelect<Resource> resourceListSelect,
                            ListSelect<String> capabilityListSelect,
                            String property, String action, String title) {

        Map<String, String> map = new HashMap<>();
        resourceListSelect.getSelectedItems().stream().forEach(r ->
                map.put(r.getUuid(), r.getDescription())
        );
        List<String> resourceNames = new ArrayList<>(map.values());
        List<String> uuids = resourceListSelect.getSelectedItems().stream().map(r -> r.getUuid()).collect(Collectors.toList());
        List<String> capabilities = new ArrayList<>(capabilityListSelect.getSelectedItems());
        List<String> values = new ArrayList<>();
        values.add(property);

        GetContextDataRequest request = new GetContextDataRequest();
        request.setUuids(uuids);
        request.setCapabilities(capabilities);

        GetContextDataResponse response = interSCityService.getContextData(request);

        List<PlotData> plotDatas = new ArrayList<>();
        for (GetDataContextResource resource : response.getResources()){
            for (String key : resource.getCapabilities().keySet()) {
                List<Map<String, Object>> contextDatas = resource.getCapabilities().get(key);
                for (Map<String, Object> contextData : contextDatas) {
                    for(String dataKey : contextData.keySet()) {
                        if (values.contains(dataKey)) {
                            PlotData data = new PlotData();
                            data.setResource(map.get(resource.getUuid()));
                            data.setCapability(key);
                            data.setKey(dataKey);
                            data.setValue(contextData.get(dataKey));
                            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.NNN    Z");
                            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
                            final ZonedDateTime parsed = ZonedDateTime.parse((String)contextData.get("date"), formatter.withZone(ZoneId.of("UTC")));
                            data.setDate(parsed.toLocalDateTime());
                            plotDatas.add(data);
                        }
                    }
                }
            }
        }

        val chart = new TimeLineChart();

        if (action.equals("Sum")) {
            Map<String, Map<String, Map<String, Map<LocalDateTime, Double>>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.groupingBy(PlotData::getDate,
                                                    Collectors.summingDouble(PlotData::getDoubleValue))))));
            createDatasetsDoubleT(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Count")) {
            Map<String, Map<String, Map<String, Map<LocalDateTime, Long>>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.groupingBy(PlotData::getDate,
                                                Collectors.counting())))));
            createDatasetsLongT(resourceNames, capabilities, values, result, chart);
        }
        else if (action.equals("Avg")) {
            Map<String, Map<String, Map<String, Map<LocalDateTime, Double>>>> result = plotDatas.stream().collect(
                    Collectors.groupingBy(PlotData::getCapability,
                            Collectors.groupingBy(PlotData::getResource,
                                    Collectors.groupingBy(PlotData::getKey,
                                            Collectors.groupingBy(PlotData::getDate,
                                            Collectors.averagingDouble(PlotData::getDoubleValue))))));
            createDatasetsDoubleT(resourceNames, capabilities, values, result, chart);
        }

        String[] labels = resourceNames.toArray(new String[resourceNames.size()]);

        //chart.setLabels(labels);
        chart.setTitle(title);
        //chart.setYLabel("Value");

        chartLayout.removeAllComponents();
        chartLayout.addComponent(chart.getChart());
    }

}
