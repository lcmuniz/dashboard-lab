package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.TextListComponent;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.annotations.Widgetset;
import com.vaadin.ui.*;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;

import java.util.ArrayList;
import java.util.List;

@Widgetset("br.ufma.lsdi.dashboardlab.dashboardlab.LeafletWidgetset")
public class MapView extends HorizontalLayout {

    private final InterSCityService interSCityService;

    private TwinColSelect<String> resourcesSelect;
    private TwinColSelect<String> capabilitiesSelect;
    private TextListComponent matchersList;
    private DateField startDate;
    private DateField endDate;

    Grid<Resource> resourceGrid;

    Gson gson = new Gson();

    public MapView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        LMap map = new LMap();
        map.setCenter(-2.476825, -44.181216);
        map.setZoomLevel(100);
        map.setSizeFull();

        LMarker marker1 = new LMarker(-2.476825, -44.181216);
        LMarker marker2 = new LMarker(-2.576825, -44.581216);

        LMarkerClusterGroup cluster = new LMarkerClusterGroup();

        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        map.addBaseLayer(layer, "OSM");

        map.addLayer(cluster);

        Button button = new Button("Search");

        button.addClickListener(e -> {

            int i = 1;
            List<Resource> resources = new ArrayList<>();
            while (true) {
                val page = interSCityService.findAllResources(i);
                if (page.size() == 0) break;
                resources.addAll(page);
                i++;
            }
            for(Resource resource: resources) {
                LMarker marker = new LMarker(resource.getLat(), resource.getLon());
                marker.setTitle(resource.getDescription());
                marker.setPopup(createPopoup(resource));
                cluster.addComponent(marker);
            }

        });

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(button);

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 0.7f);
        grid.setColumnExpandRatio(1, 0.3f);
        grid.addComponent(map);
        grid.addComponent(verticalLayout);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private String createPopoup(Resource resource) {
        String popup = "<p>";
        popup += "<b>UUID:</b> " + resource.getUuid() + "<br/>";
        popup += "<b>Description:</b> " + resource.getDescription() + "<br/>";
        popup += "<b>Status:</b> " + resource.getStatus() + "<br/>";
        popup += "<b>Capabilities:</b> " + resource.getCapabilitiesAsList() + "<br/>";
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
