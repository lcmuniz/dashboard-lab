package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

import java.util.List;

public class ActuatorCapabilitiesView extends AbstractCapabilitiesView {

    public ActuatorCapabilitiesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, indexUI, "Actuator Capabilities");
    }

    @Override
    protected List<Capability> find() {
        return interSCityService.findAllActuatorCapabilities();
    }

}
