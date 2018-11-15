package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

public class SensorsView extends AbstractResourcesView {

    public SensorsView(InterSCityService interSCityService) {
        super(interSCityService, "Sensors");
    }

    protected  void search(String value) {
        if (value.equals("")) {
            resourceGrid.setItems(interSCityService.findAllResources());
        }
        else {
            resourceGrid.setItems(
                    interSCityService.findAllSensors()
                            .stream()
                            .filter(resource -> resource.getDescription().toLowerCase().contains(value.toLowerCase()))
            );
        }

    }

}
