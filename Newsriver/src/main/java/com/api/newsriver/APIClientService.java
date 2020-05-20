package com.api.newsriver;

import org.elasticsearch.index.mapper.SearchAsYouTypeFieldMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@Service
public class APIClientService {

    final API api;

    public APIClientService(API api) {
        this.api = api;
    }

    public void getQueries() throws InterruptedException, IOException, URISyntaxException {
        /*APIQuery query=  new APIQuery.Builder().build();
        System.out.println(query.getLimit());
        System.out.println(query.getLuceneQuery());
        System.out.println(query.getSortBy());
        System.out.println(query.getSortOrder());*/

        /*
        HttpResponse<String> response = api.fetch(query);
        PrintWriter out = new PrintWriter("output.txt");
        out.println(response.statusCode());
        out.println(response.body());
        System.out.println(response.body());
        System.out.println(response.statusCode());
        */

    }
}
