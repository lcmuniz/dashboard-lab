package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.val;

public class IndexView extends VerticalLayout {

    private final InterSCityService interSCityService;

    Gson gson = AppGson.get();

    public IndexView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        VerticalLayout map = createPanel();

        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        //grid.setColumnExpandRatio(0, 0.6f);
        //grid.setColumnExpandRatio(1, 0.4f);
        grid.addComponent(map);

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");

    }

    private VerticalLayout createPanel() {

        VerticalLayout vl = new VerticalLayout();

        Label resourcesLabel = new Label("Inde: ");

        Button button = new Button("Teste");
        button.addClickListener(e -> {

/*
            DiscoveryResourcesRequest request = new DiscoveryResourcesRequest();
            request.setCapability("crime_doloso_lc");
            request.getMatchers().add("name.eq=crime_alemanha");
*/


            /*SearchResourcesRequest request = new SearchResourcesRequest();
            request.setCapability("crime_doloso_lc");
            val resources = interSCityService.searchResources(request);

            for(Resource r : resources) {
                System.out.println(r);
            }*/

//            val resource = interSCityService.getResource("27752a2a-d4f5-4300-bf69-5d8685a74ab4");
//            System.out.println(resource);

            val capability = interSCityService.getCapability("crime_doloso_lc");
            System.out.println(capability);

//            GetContextDataResponse response = interSCityService.getContextData(request);
//
//            for(GetDataContextResource r : response.getResources()) {
//                System.out.println(r.getUuid());
//                val capabilities = r.getCapabilities();
//                for (String key : capabilities.keySet()) {
//                    System.out.println("capability: " + key);
//                    List<Map<String, Object>> capability = capabilities.get(key);
//                    for (Map<String, Object> map : capability) {
//                        for (String key2 : map.keySet()) {
//                            System.out.println(key2);
//                            System.out.println(map.get(key2));
//                        }
//                    }
//                }
//            }

        });

        vl.addComponents(resourcesLabel);
        vl.addComponent(button);

        return vl;
    }

}
