package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.*;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import lombok.val;

import java.time.LocalDateTime;

public class IndexView extends VerticalLayout {

    private final InterSCityService interSCityService;

    Gson gson = AppGson.get();

    public IndexView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        VerticalLayout map = createPanel();

        addComponent(map);

        //setWidth("100%");
        //setHeight("100%");

    }

    private VerticalLayout createPanel() {

        VerticalLayout vl = new VerticalLayout();

        Label resourcesLabel = new Label("Inde: ");

        Button button = new Button("Teste");
        button.addClickListener(e -> {

            TimeLineChart chart = new TimeLineChart();
            chart.setTitle("Meu Gráfico".toUpperCase());
            chart.addData("temperature", LocalDateTime.now(), 1.0);
            chart.addData("temperature", LocalDateTime.now().plusHours(10),2.0);
            chart.addData("temperature", LocalDateTime.now().plusHours(30), 1.0);
            chart.addData("humidity", LocalDateTime.now(), 1.5);
            chart.addData("humidity", LocalDateTime.now().plusHours(10),1.5);
            chart.addData("humidity", LocalDateTime.now().plusHours(30),2.5);


//            DonutChart chart = new DonutChart();
//            chart.setTitle("Meu Gráfico".toUpperCase());
//            chart.setLabels(Arrays.asList("J", "F", "M", "A", "M"));
//
//            List<Double> data = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                data.add((double) (Math.round(Math.random() * 100)));
//            }
//
//            chart.addDataset("Dataset 1", data);
//
//            data = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                data.add((double) (Math.round(Math.random() * 100)));
//            }
//
//            chart.addDataset("Dataset 2", data);

            vl.removeAllComponents();
            vl.addComponent(button);
            vl.addComponent(chart.getChart());

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
