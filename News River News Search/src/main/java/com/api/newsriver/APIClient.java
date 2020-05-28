package com.api.newsriver;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class APIClient {

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private final String KEY;

    private APIClient(CouchDB couchDB){
        String json = couchDB.get("secrets","newsriver");
        int i= json.indexOf("key");
        KEY = json.substring(i+6,json.length()-3);
    }

    private  URI buildURL(APIQuery apiQuery) throws URISyntaxException {
        String mainURL = "https://api.newsriver.io/v2/search";
        URIBuilder url = new URIBuilder(mainURL);
        url.addParameter("query", apiQuery.getLuceneQuery());
        url.addParameter("soryBy", apiQuery.getSortBy());
        url.addParameter("sortOrder", apiQuery.getSortOrder());
        url.addParameter("limit", apiQuery.getLimit());
        return url.build();
    }

    public HttpResponse <String> fetch(APIQuery apiQuery) throws URISyntaxException, IOException, InterruptedException {
        URI uri = buildURL(apiQuery);
        System.out.println("Fetching URL:" + uri.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", KEY)
                .uri(uri)
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Exceptions produced here are handled by the Producer
    }

}
