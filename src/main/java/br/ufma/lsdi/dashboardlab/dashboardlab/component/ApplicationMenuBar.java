package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.ActuatorsView;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.IndexView;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.ResourcesView;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.SensorsView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;

public class ApplicationMenuBar extends MenuBar {

    public ApplicationMenuBar(InterSCityService interSCityService, Panel contentPanel) {

        MenuItem viewMenuItem = addItem("View", null, null);
        MenuItem indexMenuItem = viewMenuItem.addItem("Home", null, c -> {
            contentPanel.setContent(new IndexView(interSCityService));
        });
        MenuItem resourcesMenuItem = viewMenuItem.addItem("All Resources", null, c -> {
            contentPanel.setContent(new ResourcesView(interSCityService));
        });
        MenuItem sensorsMenuItem = viewMenuItem.addItem("All Sensors", null, c -> {
            contentPanel.setContent(new SensorsView(interSCityService));
        });
        MenuItem actuatorsMenuItem = viewMenuItem.addItem("All Actuators", null, c -> {
            contentPanel.setContent(new ActuatorsView(interSCityService));
        });
        viewMenuItem.addSeparator();
        MenuItem exitMenuItem = viewMenuItem.addItem("Exit", null, null);

    }

}
