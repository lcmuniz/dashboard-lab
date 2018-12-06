package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.SelectWithTextField;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.ContextDataQuery;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.ResourceQuery;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetContextDataResponse;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.contextdata.GetDataContextResource;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.ContextDataQueryRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.ResourceQueryRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.annotations.Widgetset;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.addon.leaflet.shared.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class MapView extends HorizontalLayout {

    private final InterSCityService interSCityService;
    private final ResourceQueryRepository resourceQueryRepository;
    private final ContextDataQueryRepository contextDataQueryRepository;

    Gson gson = AppGson.get();

    LMap map;
    LMarkerClusterGroup cluster;

    double zoomLevel = 15.0;

    private TextField descriptionTextField;
    private TextField capabilityTextField;
    private TextField latitudeTextField;
    private TextField longitudeTextField;
    private TextField radiusTextField;
    private TextField statusTextField;
    private TextField cityTextField;
    private TextField neighborhoodTextField;
    private TextField stateTextField;
    private TextField postalCodeTextField;
    private TextField countryTextField;

    private SelectWithTextField resourcesList;
    private SelectWithTextField capabilitiesList;
    private SelectWithTextField matchersList;
    private DateTimeField startDate;
    private DateTimeField endDate;

    public MapView(InterSCityService interSCityService,
                   ResourceQueryRepository resourceQueryRepository,
                   ContextDataQueryRepository contextDataQueryRepository,
                   IndexUI indexUI) {

        this.interSCityService = interSCityService;
        this.resourceQueryRepository = resourceQueryRepository;
        this.contextDataQueryRepository = contextDataQueryRepository;

        System.out.println(resourceQueryRepository.findAll());

        map = new LMap();
        map.setCenter(-2.476825, -44.181216);
        map.setZoomLevel(zoomLevel);
        map.setSizeFull();

        LMarker marker1 = new LMarker(-2.476825, -44.181216);
        LMarker marker2 = new LMarker(-2.576825, -44.581216);

        cluster = new LMarkerClusterGroup();

        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        map.addBaseLayer(layer, "OSM");

        map.addLayer(cluster);

        VerticalLayout form = createForm();
        //form.setMargin(false);
        form.setSpacing(false);

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setMargin(false);
        grid.setSpacing(false);
        grid.setColumnExpandRatio(0, 0.6f);
        grid.setColumnExpandRatio(1, 0.4f);
        grid.addComponent(map);
        grid.addComponent(form);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createForm() {

        VerticalLayout vl = new VerticalLayout();
        vl.setSpacing(false);
        //vl.setMargin(false);
        vl.setMargin(new MarginInfo(false,true,true,true));

        Label searchLabel = new Label("Search");
        searchLabel.addStyleName("h2");

        VerticalLayout resTab = createResTab(vl);
        VerticalLayout dataTab = createDataTab(vl);

        TabSheet tabs =  new TabSheet();
        tabs.setSizeFull();

        tabs.addTab(resTab).setCaption("Resources");
        tabs.addTab(dataTab).setCaption("Context Data");

        vl.addComponent(searchLabel);
        vl.addComponent(tabs);

        return vl;
    }

    private VerticalLayout createDataTab(VerticalLayout vl) {

        VerticalLayout dataTab = new VerticalLayout();
        dataTab.setWidth("100%");

        resourcesList = new SelectWithTextField("Resources");
        capabilitiesList = new SelectWithTextField("Capabilities");
        matchersList = new SelectWithTextField("Matchers");
        startDate = new DateTimeField("Start Date");
        endDate = new DateTimeField("End Date");
        val savedQueries = new ComboBox<ContextDataQuery>("Saved Queries");
        savedQueries.setSizeFull();

        configDeleteListener(resourcesList, capabilitiesList, matchersList);

        TabSheet tabs = new TabSheet();
        tabs.setTabsVisible(false);
        val step1Tab = new VerticalLayout();
        val step2Tab = new VerticalLayout();
        val step3Tab = new VerticalLayout();
        val step4Tab = new VerticalLayout();

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
        val step4searchButton = new Button("Search");
        val step4saveButton = new Button("Save Query");

        val popl = new VerticalLayout();

        PopupView pop = new PopupView(null, popl);
        pop.setHideOnMouseOut(false);

        savedQueries.setSizeFull();
        savedQueries.setItemCaptionGenerator(ContextDataQuery::getName);

        savedQueries.addValueChangeListener(e -> {
            val map = savedQueries.getSelectedItem().get();
            val res = gson.fromJson(map.getRequest(), GetContextDataRequest.class);
            fillWithContextDataRequest(res);
        });

        val queries = contextDataQueryRepository.findAll();
        savedQueries.setItems(queries);

        val queryNameTextField = new TextField("Query Name");
        popl.addComponent(queryNameTextField);

        val popButton = new Button("Save");
        popButton.addClickListener( e -> {

            if (queryNameTextField.isEmpty()) return;

            val request = createSearchDataContextRequest();
            ContextDataQuery q = new ContextDataQuery();
            q.setName(queryNameTextField.getValue());
            q.setRequest(gson.toJson(request));

            queries.add(q);
            queryNameTextField.clear();
            savedQueries.setItems(queries);

            contextDataQueryRepository.save(q);
            pop.setPopupVisible(false);
        });

        popl.addComponent(popButton);

        step4saveButton.addClickListener(e -> {
            pop.setPopupVisible(true);
        });


        step4previousButton.addClickListener(e -> {
            tabs.setSelectedTab(step3Tab);
            configDeleteListener(matchersList, resourcesList, capabilitiesList);
        });

        step4searchButton.addClickListener(e -> {

            cluster.removeAllComponents();

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

            val resources = response.getResources();

            for (GetDataContextResource data : resources) {
                val uuid = data.getUuid();
                val capabilitiesMap = data.getCapabilities();
                val keySet = capabilitiesMap.keySet();
                for (String key : keySet) {
                    val capability = key;
                    List<Map<String, Object>> capabilityList = capabilitiesMap.get(key);
                    double first_lat = 0, first_lon = 0;
                    for (Map<String, Object> map : capabilityList) {
                        if (map.get("lat") != null) {
                            val lat = (double) map.get("lat");
                            val lon = (double) map.get("lon");
                            if (first_lat == 0 && first_lon == 0) {
                                first_lat = lat;
                                first_lon = lon;
                            }
                            val description = (String) map.get("description");
                            LMarker marker = new LMarker(lat, lon);
                            marker.setTitle(description);
                            marker.setPopup(createContextDataHTML(uuid, capability, map));
                            cluster.addComponent(marker);
                        }
                    }

                    if (first_lat != 0 && first_lon != 0) {
                        map.flyTo(new Point(first_lat, first_lon), zoomLevel);
                    }

                }
            }

        });
        //step4saveButton.addClickListener(e -> tabs.setSelectedTab(step1Tab));

        step1Tab.addComponent(resourcesList);
        step1Tab.addComponent(step1nextButton);

        step2Tab.addComponent(capabilitiesList);
        step2Tab.addComponent(new HorizontalLayout(step2previousButton, step2nextButton));

        step3Tab.addComponent(matchersList);
        step3Tab.addComponent(new HorizontalLayout(step3previousButton, step3nextButton));

        step4Tab.addComponent(startDate);
        step4Tab.addComponent(endDate);
        step4Tab.addComponent(new HorizontalLayout(step4previousButton, step4searchButton, step4saveButton));
        step4Tab.addComponent(savedQueries);
        step4Tab.addComponent(pop);

        tabs.addTab(step1Tab);
        tabs.addTab(step2Tab);
        tabs.addTab(step3Tab);
        tabs.addTab(step4Tab);

        dataTab.addComponent(tabs);
        return dataTab;

    }

    private String createContextDataHTML(String uuid, String capability, Map<String, Object> map) {
        String html = "<p>";
        html += "<b>Resource UUID</b>: " + uuid + "<br/>";
        html += "<b>Capability</b>: " + capability + "<br/>";
        for(String key : map.keySet()) {
            html += "<b>" + key + "</b>: " + map.get(key) + "<br/>";
        }
        return html;
    }

    private void configDeleteListener(SelectWithTextField first, SelectWithTextField second, SelectWithTextField third) {
        third.removeListShortcut();
        second.removeListShortcut();
        first.addListShorcut();
    }

    private VerticalLayout createResTab(VerticalLayout vl) {
        VerticalLayout resTab = new VerticalLayout();
        resTab.setWidth("100%");

        descriptionTextField = new TextField("Description");
        descriptionTextField.setSizeFull();
        capabilityTextField = new TextField("Capability");
        capabilityTextField.setSizeFull();
        latitudeTextField = new TextField("Latitude");
        latitudeTextField.setSizeFull();
        // latitudeTextField.setWidth(140, Unit.PIXELS);
        longitudeTextField = new TextField("Longitude");
        longitudeTextField.setSizeFull();
        //longitudeTextField.setWidth(140, Unit.PIXELS);
        radiusTextField = new TextField("Radius");
        radiusTextField.setSizeFull();
        statusTextField = new TextField("Status");
        statusTextField.setSizeFull();
        cityTextField = new TextField("City");
        cityTextField.setSizeFull();
        neighborhoodTextField = new TextField("Neighborhood");
        neighborhoodTextField.setSizeFull();
        stateTextField = new TextField("State");
        stateTextField.setSizeFull();
        //stateTextField.setWidth(140, Unit.PIXELS);
        postalCodeTextField = new TextField("Postal Code");
        postalCodeTextField.setSizeFull();
        //postalCodeTextField.setWidth(140, Unit.PIXELS);
        countryTextField = new TextField("Country");
        countryTextField.setSizeFull();
        val searchButton = new Button("Search");
        val saveButton = new Button("Save Query");

        val popl = new VerticalLayout();

        PopupView pop = new PopupView(null, popl);
        pop.setHideOnMouseOut(false);

        ComboBox<ResourceQuery> savedQueries = new ComboBox<>("Saved Queries");
        savedQueries.setSizeFull();
        savedQueries.setItemCaptionGenerator(ResourceQuery::getName);

        savedQueries.addValueChangeListener(e -> {
            val map = savedQueries.getSelectedItem().get();
            val res = gson.fromJson(map.getRequest(), SearchResourcesRequest.class);
            fillWithRequest(res);
        });

        val queries = resourceQueryRepository.findAll();
        savedQueries.setItems(queries);

        val queryNameTextField = new TextField("Query Name");
        popl.addComponent(queryNameTextField);

        val popButton = new Button("Save");
        popButton.addClickListener( e -> {

            if (queryNameTextField.isEmpty()) return;

            val request = createSearchResourcesRequest();
            ResourceQuery q = new ResourceQuery();
            q.setName(queryNameTextField.getValue());
            q.setRequest(gson.toJson(request));

            queries.add(q);
            queryNameTextField.clear();
            savedQueries.setItems(queries);

            resourceQueryRepository.save(q);
            pop.setPopupVisible(false);
        });
        popl.addComponent(popButton);

        saveButton.addClickListener(e -> {
            pop.setPopupVisible(true);
        });

        resTab.addComponent(pop);

        val line1 = new HorizontalLayout(descriptionTextField, capabilityTextField);
        line1.setSizeFull();
        resTab.addComponent(line1);
        val line2 = new HorizontalLayout(latitudeTextField, longitudeTextField, radiusTextField);
        line2.setSizeFull();
        resTab.addComponent(line2);
        val line3 = new HorizontalLayout(statusTextField, cityTextField, neighborhoodTextField);
        line3.setSizeFull();
        resTab.addComponent(line3);
        val line4 = new HorizontalLayout(stateTextField, postalCodeTextField, countryTextField);
        line4.setSizeFull();
        resTab.addComponent(line4);

        val line5 = new HorizontalLayout();
        line5.addComponents(searchButton);
        line5.addComponents(saveButton);

        resTab.addComponent(line5);
        resTab.addComponent(savedQueries);

        searchButton.addClickListener(e -> {

            cluster.removeAllComponents();

            createSearchResourcesRequest();
            val request = new SearchResourcesRequest();
            if (!descriptionTextField.getValue().isEmpty()) request.setDescription(descriptionTextField.getValue());
            if (!capabilityTextField.getValue().isEmpty()) request.setCapability(capabilityTextField.getValue());
            if (!latitudeTextField.getValue().isEmpty()) request.setLat(Double.parseDouble(latitudeTextField.getValue()));
            if (!longitudeTextField.getValue().isEmpty()) request.setLon(Double.parseDouble(longitudeTextField.getValue()));
            if (!radiusTextField.getValue().isEmpty()) request.setRadius(Double.parseDouble(radiusTextField.getValue()));
            if (!statusTextField.getValue().isEmpty()) request.setStatus(statusTextField.getValue());
            if (!cityTextField.getValue().isEmpty()) request.setCity(cityTextField.getValue());
            if (!neighborhoodTextField.getValue().isEmpty()) request.setNeighborhood(neighborhoodTextField.getValue());
            if (!stateTextField.getValue().isEmpty()) request.setState(stateTextField.getValue());
            if (!postalCodeTextField.getValue().isEmpty()) request.setPostalCode(postalCodeTextField.getValue());
            if (!countryTextField.getValue().isEmpty()) request.setCountry(countryTextField.getValue());
            List<Resource> resources = interSCityService.searchResources(request);

            resources.stream().forEach(r -> {
                LMarker marker = new LMarker(r.getLat(), r.getLon());
                marker.setTitle(r.getDescription());
                marker.setPopup(r.toHTML());
                cluster.addComponent(marker);
            });

            if (resources.size() > 0) {
                map.flyTo(resources.stream().map(res -> new Point(res.getLat(), res.getLon())).findAny().get(), zoomLevel);
            }

        });

        return resTab;
    }

    private void fillWithRequest(SearchResourcesRequest res) {

        descriptionTextField.clear();
        capabilityTextField.clear();
        latitudeTextField.clear();
        longitudeTextField.clear();
        radiusTextField.clear();
        statusTextField.clear();
        cityTextField.clear();
        neighborhoodTextField.clear();
        stateTextField.clear();
        postalCodeTextField.clear();
        countryTextField.clear();

        if (res.getDescription() != null) descriptionTextField.setValue(res.getDescription());
        if (res.getCapability() != null) capabilityTextField.setValue(res.getCapability());
        if (res.getLat() != null) latitudeTextField.setValue(res.getLat()+"");
        if (res.getLon() != null) longitudeTextField.setValue(res.getLon()+"");
        if (res.getRadius() != null) radiusTextField.setValue(res.getRadius()+"");
        if (res.getStatus() != null) statusTextField.setValue(res.getStatus());
        if (res.getCity() != null) cityTextField.setValue(res.getCity());
        if (res.getNeighborhood() != null) neighborhoodTextField.setValue(res.getNeighborhood());
        if (res.getState() != null) stateTextField.setValue(res.getState());
        if (res.getPostalCode() != null) postalCodeTextField.setValue(res.getPostalCode());
        if (res.getCountry() != null) countryTextField.setValue(res.getCountry());
    }

    private void fillWithContextDataRequest(GetContextDataRequest res) {

        resourcesList.getItems().clear();
        capabilitiesList.getItems().clear();
        matchersList.getItems().clear();
        startDate.clear();
        endDate.clear();

        if (res.getUuids() != null) resourcesList.getItems().addAll(res.getUuids());
        if (res.getCapabilities() != null) capabilitiesList.getItems().addAll(res.getCapabilities());

        if (res.getMatchers() != null) {
            for (String key : res.getMatchers().keySet()) {
                matchersList.getItems().add(key + "=" + res.getMatchers().get(key));
            }
        }

        if (res.getStartDate() != null) startDate.setValue(res.getStartDate());
        if (res.getEndDate() != null) endDate.setValue(res.getEndDate());
    }

    private GetContextDataRequest createSearchDataContextRequest() {
        val request = new GetContextDataRequest();
        if (!resourcesList.getItems().isEmpty()) request.setUuids(resourcesList.getItems());
        if (!capabilitiesList.getItems().isEmpty()) request.setCapabilities(capabilitiesList.getItems());
        if (!matchersList.getItems().isEmpty()) {
            Map<String, Object> map1 = new HashMap<>();
            matchersList.getItems().forEach(m -> {
                val match = m.split("=");
                map1.put(match[0], match[1]);
            });
            request.setMatchers(map1);
        }
        if (startDate.getValue() != null) request.setStartDate(startDate.getValue());
        if (endDate.getValue() != null) request.setEndDate(endDate.getValue());
        return request;
    }

    private SearchResourcesRequest createSearchResourcesRequest() {
        val request = new SearchResourcesRequest();
        if (!descriptionTextField.getValue().isEmpty()) request.setDescription(descriptionTextField.getValue());
        if (!capabilityTextField.getValue().isEmpty()) request.setCapability(capabilityTextField.getValue());
        if (!latitudeTextField.getValue().isEmpty()) request.setLat(Double.parseDouble(latitudeTextField.getValue()));
        if (!longitudeTextField.getValue().isEmpty()) request.setLon(Double.parseDouble(longitudeTextField.getValue()));
        if (!radiusTextField.getValue().isEmpty()) request.setRadius(Double.parseDouble(radiusTextField.getValue()));
        if (!statusTextField.getValue().isEmpty()) request.setStatus(statusTextField.getValue());
        if (!cityTextField.getValue().isEmpty()) request.setCity(cityTextField.getValue());
        if (!neighborhoodTextField.getValue().isEmpty()) request.setNeighborhood(neighborhoodTextField.getValue());
        if (!stateTextField.getValue().isEmpty()) request.setState(stateTextField.getValue());
        if (!postalCodeTextField.getValue().isEmpty()) request.setPostalCode(postalCodeTextField.getValue());
        if (!countryTextField.getValue().isEmpty()) request.setCountry(countryTextField.getValue());
        return request;
    }

}
