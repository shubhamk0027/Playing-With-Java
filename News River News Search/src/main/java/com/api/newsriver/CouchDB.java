package com.api.newsriver;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CouchDB {

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final Logger logger = LoggerFactory.getLogger(CouchDB.class);
    String baseUrl = "http://shubham:fakepassword@127.0.0.1:5984/"; // local database

    private CouchDB(@Value("${TOPIC}") String topic){
        try {
            URI url = new URIBuilder(baseUrl+topic).build();
            HttpRequest request = HttpRequest.newBuilder().uri(url).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info(response.body());
        }catch(URISyntaxException | IOException | InterruptedException e){
            logger.error("FAILED connect to the client");
        }
    }


    public void dump(String database, String key, String jsonString) {
        try {
            URI url = new URIBuilder(baseUrl+database+"/"+key+"/").build();
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                    .uri(url)
                    .build();
            HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
            logger.info(response.body());
        }catch(URISyntaxException | InterruptedException | IOException e ) {
            e.printStackTrace();
        }
    }

    public String get(String database, String key){
        try {
            URI url = new URIBuilder(baseUrl + database + "/" + key).build();
            HttpRequest request= HttpRequest.newBuilder()
                    .GET()
                    .uri(url)
                    .build();
            HttpResponse<String> response= httpClient.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body().toString();
        }catch(URISyntaxException | InterruptedException | IOException e){
            logger.info("Could not fetch the key!");
            e.printStackTrace();
            return "-1";
        }
    }
}
