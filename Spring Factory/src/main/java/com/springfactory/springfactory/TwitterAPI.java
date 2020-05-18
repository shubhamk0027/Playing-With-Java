package com.springfactory.springfactory;

import org.springframework.stereotype.Component;

@Component
public class TwitterAPI extends APIDetect{

    private static final String uniqueName="TWITTER-API";
    private static final TwitterAPI INSTANCE = new TwitterAPI();
    private TwitterAPI(){/*This construction can never be called outside this class*/}

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

    public static TwitterAPI getInstance(){
        return INSTANCE;
    }

}
