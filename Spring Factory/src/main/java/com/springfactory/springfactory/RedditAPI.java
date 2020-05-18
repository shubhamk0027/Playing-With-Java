package com.springfactory.springfactory;

import org.springframework.stereotype.Component;

@Component
public class RedditAPI extends APIDetect{

    private static final String uniqueName="REDDIT-API";
    private static final RedditAPI INSTANCE = new RedditAPI();
    private RedditAPI(){/*This construction can never be called outside this class*/}

    @Override
    public String name() {
        return uniqueName;
    }

    @Override
    public String detect(String text) throws Exception {
        if(!Validate(text)){ throw new Exception("Text is not valid!");}
        /*Process the text specific to the API*/
        return "DONE";
    }

    public static RedditAPI getInstance(){
        return INSTANCE;
    }

}
