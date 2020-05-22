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

        private static final HttpClient httpClient= HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        private static final String KEY= "sBBqsGXiYgF0Db5OV5tAw7dp-iJhOfRzH26dz4CLNl8JkSYgwl8l_EKKUGmR73Gen2pHZrSf1gT2PUujH1YaQA";
        private static final String mainURL="https://api.newsriver.io/v2/search";

        private static URI buildURL(APIQuery apiQuery) throws URISyntaxException {
                URIBuilder url = new URIBuilder(mainURL);
                url.addParameter("query",apiQuery.getLuceneQuery());
                url.addParameter("soryBy",apiQuery.getSortBy());
                url.addParameter("sortOrder",apiQuery.getSortOrder());
                url.addParameter("limit",apiQuery.getLimit());
                return url.build();
        }

        public static HttpResponse<String> fetch(APIQuery apiQuery) throws URISyntaxException, IOException, InterruptedException {
                URI uri = buildURL(apiQuery);
                System.out.println("Fetching URL:"+uri.toString());
                HttpRequest request= HttpRequest.newBuilder()
                        .GET()
                        .header("Authorization",KEY)
                        .uri(uri)
                        .build();
                HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
                return response;
        }

}
