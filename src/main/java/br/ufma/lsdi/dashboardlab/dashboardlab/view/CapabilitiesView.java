package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

import java.util.List;

public class CapabilitiesView extends  AbstractCapabilitiesView {

    public CapabilitiesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, indexUI, "Capabilities");
    }

    @Override
    protected List<Capability> find() {
        return interSCityService.findAllCapabilities();
    }

}
