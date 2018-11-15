package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.IndexView;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.ResourcesView;
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
        MenuItem sensorsMenuItem = viewMenuItem.addItem("Sensors", null, null);
        viewMenuItem.addSeparator();
        MenuItem exitMenuItem = viewMenuItem.addItem("Exit", null, null);

    }

}
