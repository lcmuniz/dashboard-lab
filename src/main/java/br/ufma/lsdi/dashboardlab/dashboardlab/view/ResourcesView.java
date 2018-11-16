package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;

public class ResourcesView extends AbstractResourcesView {

    public ResourcesView(InterSCityService interSCityService, IndexUI indexUI) {
        super(interSCityService, "Resources", indexUI);
    }

    @Override
    protected void search(String value) {
        if (value.equals("")) {
            resourceGrid.setItems(interSCityService.findAllResources());
        }
        else {
            resourceGrid.setItems(
                    interSCityService.findAllResources()
                            .stream()
                            .filter(resource -> resource.getDescription().toLowerCase().contains(value.toLowerCase()))
            );
        }

    }

}
