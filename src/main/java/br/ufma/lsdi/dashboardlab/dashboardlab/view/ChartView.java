package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.*;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChartView extends VerticalLayout {

    private final InterSCityService interSCityService;

    VerticalLayout chartLayout;

    public ChartView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(0, 0.65f);
        grid.setColumnExpandRatio(1, 0.35f);

        chartLayout = new VerticalLayout();
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

        Label label = new Label("Chart Options");
        label.addStyleName("h2");

        vl.addComponent(label);

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
                plotChart(resourceListSelect,
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

        return vl;
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

    private void plotChart(ListSelect<Resource> resourceListSelect,
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

    private void createDatasetsDouble(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Double>>> result, Chart chart) {
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

    private void createDatasetsDoubleT(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Map<LocalDateTime, Double>>>> result, Chart chart) {
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

    private void createDatasetsLongT(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Map<LocalDateTime, Long>>>> result, Chart chart) {
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

    private void createDatasetsLong(List<String> resourceNames, List<String> capabilities, List<String> values, Map<String, Map<String, Map<String, Long>>> result, Chart chart) {
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
