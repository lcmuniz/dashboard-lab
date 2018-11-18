package br.ufma.lsdi.dashboardlab.dashboardlab;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resources;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DashboardLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(DashboardLabApplication.class, args);
    }

}
