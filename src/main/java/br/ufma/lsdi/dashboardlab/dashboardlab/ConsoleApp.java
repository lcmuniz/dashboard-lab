package br.ufma.lsdi.dashboardlab.dashboardlab;

import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.ResourceData;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component
public class ConsoleApp implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        //update();
    }

    private void update() throws IOException {

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/bairros.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();


        String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
        String[] nextRecord;
        Gson gson = new Gson();

        while ((nextRecord = csvReader.readNext()) != null) {

            val bairro = nextRecord[0];
            val uuid = nextRecord[1];
            val lat = nextRecord[2];
            val lon = nextRecord[3];

            try {
                val url = baseUrl + "catalog/resources/{uuid}";
                Resource resource = new Resource();
/*
                resource.setUuid(uuid);
                resource.setDescription(bairro);
                resource.setLat(new Double(lat));
                resource.setLon(new Double(lon));
                resource.setStatus("active");
*/
                String[] capabilities = {"crime_doloso", "crime_culposo"};
                resource.setCapabilities(capabilities);
                String put = gson.toJson(resource);
                val json = Unirest.put(url)
                        .header("accept", "application/json")
                        .body(put).asJson().getBody().toString();
                val resourceData = gson.fromJson(json, ResourceData.class);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }


        }

    }

}