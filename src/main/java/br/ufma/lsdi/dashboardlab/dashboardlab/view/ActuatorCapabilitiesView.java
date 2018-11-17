package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Capability;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Optional;

public class ActuatorCapabilitiesView extends AbstractCapabilitiesView {

    public ActuatorCapabilitiesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, indexUI, "Actuator Capabilities");
    }

    @Override
    protected List<Capability> find() {
        return interSCityService.findAllActuatorCapabilities();
    }

}
