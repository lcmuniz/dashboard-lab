package br.ufma.lsdi.dashboardlab.dashboardlab;

import br.ufma.lsdi.dashboardlab.dashboardlab.gson.DateSerializer;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Crime;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.CrimeData;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.Resource;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.ResourceData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ConsoleApp implements CommandLineRunner {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DateSerializer())
            .create();

    @Override
    public void run(String... args) throws Exception {
        //updateResource();
        updateResource();
    }

    private void find() {

    }

    private void updateResource() throws IOException {

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/bairros.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();


        String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {

            val bairro = nextRecord[0];
            val lat = nextRecord[1];
            val lon = nextRecord[2];
            val uuid = UUID.randomUUID().toString();

            System.out.println(uuid+";"+bairro+";"+lat+";"+lon);

        }

    }

    private void update() throws IOException {

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/crimes_dolosos.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();


        String baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
        String[] nextRecord;

        int i = 0;
        while ((nextRecord = csvReader.readNext()) != null) {

            val capability = nextRecord[0];
            val bairro = nextRecord[1];
            val uuid = nextRecord[2];
            val lat = nextRecord[3];
            val lon = nextRecord[4];
            val timestamp = nextRecord[5];

            try {
                val url = baseUrl + "adaptor/resources/{uuid}/data/{capability}";
                Crime crime = new Crime();
                crime.setLat(new Double(lat));
                crime.setLon(new Double(lon));
                DateTimeFormatter f = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                crime.setTimestamp(LocalDateTime.parse(timestamp,f));
                CrimeData crimeData = new CrimeData();
                crimeData.getData().add(crime);

                String put = gson.toJson(crimeData);
                val json = Unirest.post(url)
                        .header("accept", "application/json")
                        .header("Content-Type", "application/json")
                        .routeParam("uuid", uuid)
                        .routeParam("capability", capability)
                        .body(put).asJson().getBody().toString();
                System.out.println(json + i++);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        System.out.println("DIM");


    }

}