package br.ufma.lsdi.dashboardlab.dashboardlab;

import br.ufma.lsdi.dashboardlab.dashboardlab.gson.DateSerializer;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@Component
public class ConsoleApp implements CommandLineRunner {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new DateSerializer())
            .create();

    @Override
    public void run(String... args) throws Exception {
        //resources();
       // crimes_culposos();
        //crimes_dolosos();
    }

    private void resources() throws IOException {

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/bairros.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {

            val uuid = nextRecord[0];
            val bairro = nextRecord[1];
            val lat = nextRecord[2];
            val lon = nextRecord[3];

            val post = "{\"data\":{" +
                    " \"uuid\":\"" + uuid + "\"," +
                    "\"capabilities\": [\"crime_culposo_lc\",\"crime_doloso_lc\"]," +
                    " \"status\":\"active\"," +
                    " \"description\":\"" + bairro + "\"," +
                    " \"lat\":" + lat + "," +
                    " \"lon\":" + lon + "" +
                    "}}";

            //postResource(post);

            System.out.println(post);
            //System.out.println(uuid+";"+bairro+";"+lat+";"+lon);

        }

    }

    private void postResource(String post) {
        try {
            val baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
            val url = baseUrl + "adaptor/resources";
            val response = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(post).asJson().getBody().toString();
            System.out.println(response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getMap() throws IOException {

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/bairros.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] nextRecord;

        Map<String, String> map = new HashMap<>();
        while ((nextRecord = csvReader.readNext()) != null) {

            val uuid = nextRecord[0];
            val bairro = nextRecord[1];
            map.put(bairro, uuid);

        }

        return map;

    }

    private void crimes_culposos() throws IOException {

        //Map<String, String> map = getMap();

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/crimes_culposos.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {

            val capability = nextRecord[0];
            val lat = nextRecord[1];
            val lon = nextRecord[2];
            val timestamp = nextRecord[3];
            val uuid = nextRecord[4];;
            val bairro = nextRecord[5];
            val latr = nextRecord[6];
            val lonr = nextRecord[7];

            val post = "{" +
                    "\"data\": [" +
                    "{" +
                    "\"uuid\": \""+UUID.randomUUID().toString()+"\"," +
                    "\"capability\": \""+capability+"\"," +
                    "\"name\": \"crime_"+bairro.replace(" ","_").toLowerCase()+"\"," +
                    "\"group\": \"crime\"," +
                    "\"value\": 1," +
                    "\"lat\": "+lat+"," +
                    "\"lon\": "+lon+"," +
                    "\"timestamp\": \""+timestamp+"\"," +
                    "\"resource_id\": \""+uuid+"\"," +
                    "\"resource_description\": \""+bairro+"\"," +
                    "\"resource_lat\": "+latr+"," +
                    "\"resource_lon\": "+lonr+"" +
                    "}" +
                    "]" +
                    "}";

            postCrime(uuid, capability, post);

            System.out.println(post);
           // System.out.println(capability+";"+lat+";"+lon+";"+timestamp+";"+uuid+";"+bairro+";"+latr+";"+lonr);

        }

    }

    private void postCrime(String uuid, String capability, String post) {
        try {
            val baseUrl = "http://cidadesinteligentes.lsdi.ufma.br/";
            val url = baseUrl + "adaptor/resources/{uuid}/data/{capability}";
            val response = Unirest.post(url)
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .routeParam("uuid", uuid)
                    .routeParam("capability", capability)
                    .body(post).asJson().getBody().toString();
            System.out.println(response);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void crimes_dolosos() throws IOException {

        //Map<String, String> map = getMap();

        val dir = new java.io.File( "." ).getCanonicalPath();

        val fr = new FileReader(dir + "/src/main/resources/static/crimes_dolosos.csv");

        val parser = new CSVParserBuilder().withSeparator(';').build();
        val csvReader = new CSVReaderBuilder(fr).withCSVParser(parser).build();

        String[] nextRecord;

        while ((nextRecord = csvReader.readNext()) != null) {

            val capability = nextRecord[0];
            val lat = nextRecord[1];
            val lon = nextRecord[2];
            val timestamp = nextRecord[3];
            val uuid = nextRecord[4];
            val bairro = nextRecord[5];
            val latr = nextRecord[6];
            val lonr = nextRecord[7];

            val post = "{" +
                    "\"data\": [" +
                    "{" +
                    "\"uuid\": \""+UUID.randomUUID().toString()+"\"," +
                    "\"capability\": \""+capability+"\"," +
                    "\"name\": \"crime_"+bairro.replace(" ","_").toLowerCase()+"\"," +
                    "\"group\": \"crime\"," +
                    "\"value\": 1," +
                    "\"lat\": "+lat+"," +
                    "\"lon\": "+lon+"," +
                    "\"timestamp\": \""+timestamp+"\"," +
                    "\"resource_id\": \""+uuid+"\"," +
                    "\"resource_description\": \""+bairro+"\"," +
                    "\"resource_lat\": "+latr+"," +
                    "\"resource_lon\": "+lonr+"" +
                    "}" +
                    "]" +
                    "}";

            //postCrime(uuid, capability, post);

            System.out.println(post);
            //System.out.println(capability+";"+lat+";"+lon+";"+timestamp+";"+uuid+";"+bairro+";"+latr+";"+lonr);

        }

    }

}
