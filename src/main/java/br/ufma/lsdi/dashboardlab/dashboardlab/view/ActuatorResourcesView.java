package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

import java.util.List;

public class ActuatorResourcesView extends AbstractSAResourcesView {

    public ActuatorResourcesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, "Actuators", indexUI);
    }

    @Override
    protected List<Resource> findAll() {
        return interSCityService.findAllActuatorResources();
    }

}
