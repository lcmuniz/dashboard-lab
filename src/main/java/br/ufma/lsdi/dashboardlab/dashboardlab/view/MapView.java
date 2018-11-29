package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.annotations.Widgetset;
import com.vaadin.ui.*;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.ui.NumberField;

import java.util.List;

@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class MapView extends HorizontalLayout {

    private final InterSCityService interSCityService;

    LMap map;
    LMarkerClusterGroup cluster;

    double zoomLevel = 15.0;

    public MapView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

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

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setMargin(false);
        grid.setSpacing(false);
        grid.setColumnExpandRatio(0, 0.6f);
        grid.setColumnExpandRatio(1, 0.4f);
        grid.addComponent(map);
        grid.addComponent(form);

        addComponent(grid);

        //setSizeFull();
        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createForm() {

        VerticalLayout vl = new VerticalLayout();
        //vl.setMargin(false);
        vl.setSpacing(false);

        Label searchLabel = new Label("Search");
        searchLabel.addStyleName("h2");

        VerticalLayout resTab = new VerticalLayout();
        resTab.setWidth("100%");
        VerticalLayout dataTab = new VerticalLayout();
        dataTab.setWidth("100%");
        TabSheet tabs =  new TabSheet();
        tabs.setSizeFull();

        tabs.addTab(resTab).setCaption("Resources");
        tabs.addTab(dataTab).setCaption("Context Data");

        vl.addComponent(searchLabel);
        vl.addComponent(tabs);

        val descriptionTextField = new TextField("Description");
        descriptionTextField.setSizeFull();
        val capabilityTextField = new TextField("Capability");
        capabilityTextField.setSizeFull();
        val latitudeTextField = new TextField("Latitude");
        latitudeTextField.setSizeFull();
       // latitudeTextField.setWidth(140, Unit.PIXELS);
        val longitudeTextField = new TextField("Longitude");
        longitudeTextField.setSizeFull();
        //longitudeTextField.setWidth(140, Unit.PIXELS);
        val radiusTextField = new TextField("Radius");
        radiusTextField.setSizeFull();
        val statusTextField = new TextField("Status");
        statusTextField.setSizeFull();
        val cityTextField = new TextField("City");
        cityTextField.setSizeFull();
        val neighborhoodTextField = new TextField("Neighborhood");
        neighborhoodTextField.setSizeFull();
        val stateTextField = new TextField("State");
        stateTextField.setSizeFull();
        //stateTextField.setWidth(140, Unit.PIXELS);
        val postalCodeTextField = new TextField("Postal Code");
        postalCodeTextField.setSizeFull();
        //postalCodeTextField.setWidth(140, Unit.PIXELS);
        val countryTextField = new TextField("Country");
        countryTextField.setSizeFull();
        val button = new Button("Search");

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
        resTab.addComponent(button);

        button.addClickListener(e -> {
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

        return vl;
    }

    private String createPopoup(Resource resource) {
        String popup = "<p>";
        popup += "<b>UUID:</b> " + resource.getUuid() + "<br/>";
        popup += "<b>Description:</b> " + resource.getDescription() + "<br/>";
        popup += "<b>Status:</b> " + resource.getStatus() + "<br/>";
        popup += "<b>Capabilities:</b> " + resource.getCapabilities().stream().limit(10) + "<br/>";
        popup += "<b>Uri:</b> " + resource.getUri() + "<br/>";
        popup += "<b>Latitude:</b> " + resource.getLat() + "<br/>";
        popup += "<b>Longitude:</b> " + resource.getLon() + "<br/>";
        popup += "<b>City:</b> " + resource.getCity() + "<br/>";
        popup += "<b>Neighborhood:</b> " + resource.getNeighborhood() + "<br/>";
        popup += "<b>Postal Code:</b> " + resource.getPostalCode() + "<br/>";
        popup += "<b>State:</b> " + resource.getState() + "<br/>";
        popup += "<b>Collect Interval:</b> " + resource.getCollectInterval() + "<br/>";

        popup += "</p>";
        return popup;
    }

}
