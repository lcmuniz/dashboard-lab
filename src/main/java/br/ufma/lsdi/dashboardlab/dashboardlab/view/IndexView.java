package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.TextListComponent;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.collector.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.ui.*;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.markercluster.LMarkerClusterGroup;
import org.vaadin.ui.NumberField;

import java.util.ArrayList;
import java.util.List;

public class IndexView extends VerticalLayout {

    private final InterSCityService interSCityService;

    Gson gson = AppGson.get();

    public IndexView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        VerticalLayout map = createPanel();

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        //grid.setColumnExpandRatio(0, 0.6f);
        //grid.setColumnExpandRatio(1, 0.4f);
        grid.addComponent(map);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createPanel() {

        VerticalLayout vl = new VerticalLayout();

        Label resourcesLabel = new Label("Resources: ");
        vl.addComponents(resourcesLabel);

        return vl;
    }

    private VerticalLayout createPanel2() {

        VerticalLayout vl = new VerticalLayout();

        Label resourcesLabel = new Label("Resources: ");
        resourcesLabel.addStyleName("h2");
        int nr = interSCityService.findAllResources().size();
        Label numberResources = new Label(nr+"");

        Label capabilitiesLabel = new Label("Capabilities: ");
        capabilitiesLabel.addStyleName("h2");
        int nc = interSCityService.findAllCapabilities().size();
        Label numberCapabilities = new Label(nc+"");

        vl.addComponents(
                resourcesLabel,
                numberResources,
                capabilitiesLabel,
                numberCapabilities
        );

        return vl;
    }


}
