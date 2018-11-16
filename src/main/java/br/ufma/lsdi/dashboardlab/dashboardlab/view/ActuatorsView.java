package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

public class ActuatorsView extends AbstractResourcesView {

    public ActuatorsView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, "Actuators", indexUI);
    }

    protected  void search(String value) {
        if (value.equals("")) {
            resourceGrid.setItems(interSCityService.findAllResources());
        }
        else {
            resourceGrid.setItems(
                    interSCityService.findAllActuators()
                            .stream()
                            .filter(resource -> resource.getDescription().toLowerCase().contains(value.toLowerCase()))
            );
        }

    }

}
