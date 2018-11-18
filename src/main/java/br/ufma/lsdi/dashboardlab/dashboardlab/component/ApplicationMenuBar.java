package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.*;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;

public class ApplicationMenuBar extends MenuBar {

    public ApplicationMenuBar(InterSCityService interSCityService, Panel contentPanel, IndexUI indexUI) {

        MenuItem resourcesMenuItem = addItem("Resources", null, null);
        resourcesMenuItem.addItem("Home", null, c -> {
            contentPanel.setContent(new IndexView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("All Resources", null, c -> {
            contentPanel.setContent(new ResourcesView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("All Resources w/ Sensors", null, c -> {
            contentPanel.setContent(new SensorResourcesView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("All Resources w/ Actuators", null, c -> {
            contentPanel.setContent(new ActuatorResourcesView(interSCityService, indexUI));
        });
        resourcesMenuItem.addSeparator();
        resourcesMenuItem.addItem("Exit", null, null);

        MenuItem capabilitiesMenuItem = addItem("Capabilities", null, null);
        capabilitiesMenuItem.addItem("All Capabilities", null, c -> {
            contentPanel.setContent(new CapabilitiesView(interSCityService, indexUI));
        });
        capabilitiesMenuItem.addItem("All Sensor Capabilities", null, c -> {
            contentPanel.setContent(new SensorCapabilitiesView(interSCityService, indexUI));
        });
        capabilitiesMenuItem.addItem("All Actuator Capabilities", null, c -> {
            contentPanel.setContent(new ActuatorCapabilitiesView(interSCityService, indexUI));
        });

        MenuItem dataMenuItem = addItem("Context Data", null, null);
        dataMenuItem.addItem("All Context Data", null, c -> {
            contentPanel.setContent(new DataView(interSCityService, indexUI));
        });
        dataMenuItem.addItem("Most Recent Context Data", null, c -> {
            contentPanel.setContent(new DataView(interSCityService, indexUI));
        });

        MenuItem mapItem = addItem("Map", null, null);
        mapItem.addItem("Map View", null, c -> {
            contentPanel.setContent(new MapView(interSCityService, indexUI));
        });

        setSizeFull();
    }

}
