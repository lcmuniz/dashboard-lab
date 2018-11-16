package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.*;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;

public class ApplicationMenuBar extends MenuBar {

    public ApplicationMenuBar(InterSCityService interSCityService, Panel contentPanel, IndexUI indexUI) {

        MenuItem viewMenuItem = addItem("View", null, null);
        MenuItem indexMenuItem = viewMenuItem.addItem("Home", null, c -> {
            contentPanel.setContent(new IndexView(interSCityService, indexUI));
        });
        MenuItem resourcesMenuItem = viewMenuItem.addItem("All Resources", null, c -> {
            contentPanel.setContent(new ResourcesView(interSCityService, indexUI));
        });
        MenuItem sensorsMenuItem = viewMenuItem.addItem("All Sensors", null, c -> {
            contentPanel.setContent(new SensorsView(interSCityService, indexUI));
        });
        MenuItem actuatorsMenuItem = viewMenuItem.addItem("All Actuators", null, c -> {
            contentPanel.setContent(new ActuatorsView(interSCityService, indexUI));
        });
        MenuItem capabilitiesMenuItem = viewMenuItem.addItem("All Capabilities", null, c -> {
            contentPanel.setContent(new CapabilitiesView(interSCityService, indexUI));
        });
        viewMenuItem.addSeparator();
        MenuItem exitMenuItem = viewMenuItem.addItem("Exit", null, null);

    }

}
