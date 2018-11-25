package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.interscitymodel.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.annotations.Widgetset;
import com.vaadin.ui.*;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.ui.NumberField;

import java.util.List;

@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class MapView extends HorizontalLayout {

    private final InterSCityService interSCityService;

    LMarkerClusterGroup cluster;

    public MapView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        LMap map = new LMap();
        map.setCenter(-2.476825, -44.181216);
        map.setZoomLevel(100);
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

        Label searchLabel = new Label("Search");
        searchLabel.addStyleName("h2");

        Label locationLabel = new Label("Select DataCollectorLocation");
        NumberField latitudeNumberField = new NumberField("Latitude");
        NumberField longitudeNumberField = new NumberField("Longitude");
        NumberField radiusNumberField = new NumberField("Radius");

        TextField capabilitiesTextField = new TextField("Capabilities");

        vl.addComponent(searchLabel);
        vl.addComponent(capabilitiesTextField);
        vl.addComponent(locationLabel);
        vl.addComponent(latitudeNumberField);
        vl.addComponent(longitudeNumberField);
        vl.addComponent(radiusNumberField);

        Button button = new Button("Search");
        Button button2 = new Button("Search 2");

        // pesquisa por json
        button.addClickListener(e -> {
            /*String json = interSCityService.findAllData(capabilitiesTextField.getValue());
            DataCollectorResponse response = new DataCollectorResponse(json);

            for(DataCollectorResource resource: response.getResources()) {
                for (DataCollectorCapability capabilities : resource.getCapabilities()) {
                    for (DataCollectorContextData contextData : capabilities.getDataList()) {
                        DataCollectorLocation location = contextData.getLocation();

                        if (location != null) {
                            LMarker marker = new LMarker(location.getLat(), location.getLon());
                            marker.setTitle(contextData.getCapability().getName());
                            marker.setPopup(contextData.getCapability().toHTML());
                            cluster.addComponent(marker);
                        }
                    }
                }
            }*/

        });

        // pesquisa pelos campos do formulario
        button2.addClickListener(e -> {

            List<Resource> resources = interSCityService.getAllResources();
            for(Resource resource: resources) {
                LMarker marker = new LMarker(resource.getLat(), resource.getLon());
                marker.setTitle(resource.getDescription());
                marker.setPopup(createPopoup(resource));
                cluster.addComponent(marker);
            }

        });

        vl.addComponent(button);
        vl.addComponent(button2);

        return vl;
    }

    private String createPopoup(Resource resource) {
        String popup = "<p>";
        popup += "<b>UUID:</b> " + resource.getUuid() + "<br/>";
        popup += "<b>Description:</b> " + resource.getDescription() + "<br/>";
        popup += "<b>Status:</b> " + resource.getStatus() + "<br/>";
        //popup += "<b>Capabilities:</b> " + resource.getCapabilitiesAsList() + "<br/>";
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
