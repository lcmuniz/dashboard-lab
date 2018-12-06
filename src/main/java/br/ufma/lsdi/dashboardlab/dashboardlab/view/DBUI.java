package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.TimeLineChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.component.AppGson;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.ResourceData;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.DBContextData;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.database.DBResource;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.DBContextDataRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.repository.DBResourceRepository;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lombok.val;

import java.time.LocalDateTime;
import java.util.*;

@SpringUI(path = "/db")
@Theme("valo")
public class DBUI extends UI {

    private DBResourceRepository resRepo;
    private DBContextDataRepository cdRepo;
    private InterSCityService service;

    public DBUI(DBResourceRepository resRepo, DBContextDataRepository cdRepo, InterSCityService service) {
        this.resRepo = resRepo;
        this.cdRepo = cdRepo;
        this.service = service;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        VerticalLayout vl = new VerticalLayout();
        Button btn = new Button("OK");
        btn.addClickListener(e -> createData());
        vl.addComponent(btn);
        setContent(vl);

    }

    private void createData() {

        Gson gson = new Gson();

        try {

            List<DBResource> data = resRepo.findAll();
            data.stream().filter(d ->
                    !d.getDescription().contains("Alemanha")
                            && !d.getDescription().contains("Alto do Calhau")
                            && !d.getDescription().contains("Anil")
            );
            for (DBResource res: data) {

                for (int d = 1; d <= 28; d++) {
                    for (int m = 1; m <= 12; m++) {

                        String sd = d <10 ? "0"+d : d+"";
                        String sm = m <10 ? "0"+m : m+"";

                        String json = "{" +
                                "\"data\":[" +
                                "{"+
                                "\"resource_uuid\":\"" + res.getUuid() + "\"," +
                                "\"resource_description\":\"" + res.getDescription() + "\"," +
                                "\"capability\":\"city_weather_info\"," +
                                "\"date\":\"2018-"+sm+"-"+sd+"T00:00\"," +
                                "\"lat\": "+res.getLat()+"," +
                                "\"lon\": "+res.getLon()+"," +
                                "\"temperature\":"+getTemp()+"," +
                                "\"precipitation\":"+getPrec()+"," +
                                "\"bairro\":\""+getDesc(res)+"\"," +
                                "\"population\":"+getPop(res)+"" +
                                "}"+
                                "]"+
                                "}";

                        String response = Unirest.post("http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources/{uuid}/data/{capability}")
                                .header("accept", "application/json")
                                .header("content-type", "application/json")
                                .routeParam("uuid", res.getUuid())
                                .routeParam("capability", "city_weather_info")
                                .body(json).asJson().getBody().toString();
                        System.out.println(res.getDescription() + " - " + d + "/" + m);

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getDesc(DBResource res) {
        return res.getDescription()
                .replace("_1", "")
                .replace("_2", "")
                .replace("_3", "")
                .replace("_4", "")
                .replace("_5", "");
    }

    private String getPop(DBResource res) {
        Map<String, String> map = new HashMap<>();
        map.put("Alemanha", "15000");
        map.put("Alto do Calhau", "20000");
        map.put("Anil","15000");
        map.put("Anjo da Guarda", "25000");
        map.put("Apeadouro", "13000");
        map.put("Areinha", "5000");
        map.put("Aurora", "18000");
        map.put("Bacanga", "20000");
        map.put("Belira", "7000");
        map.put("Camboa", "16000");
        map.put("Centro", "21000");
        map.put("Cidade Olímpica", "25000");
        map.put("Cidade Operária", "27000");
        map.put("Cohab", "19000");
        map.put("Cohama", "11000");
        map.put("Cohatrac", "23000");
        map.put("Coroadinho", "19000");
        map.put("Liberdade", "7000");
        map.put("Lira", "8000");
        map.put("Madre Deus", "12000");
        map.put("Maracanã", "21000");
        map.put("Renascença", "18000");
        map.put("Sá Viana", "10000");
        map.put("São Cristóvão","12000");
        map.put("Tirirical", "8000");
        map.put("Vila Embratel", "11000");
        map.put("Vinhais", "13000");

        return map.get(getDesc(res));
    }

    private String getPrec() {
        return ""+ ((int) (Math.random() * 10));
    }

    private String getTemp() {
        return ""+ (28 + (int) (Math.random() * 12));
    }

}
