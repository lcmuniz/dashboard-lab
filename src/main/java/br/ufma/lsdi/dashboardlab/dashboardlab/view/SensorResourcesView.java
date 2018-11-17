package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

import java.util.List;

public class SensorResourcesView extends AbstractSAResourcesView {

    public SensorResourcesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, "Sensors", indexUI);
    }

    @Override
    protected List<Resource> findAll() {
        return interSCityService.findAllSensorResources();
    }

}
