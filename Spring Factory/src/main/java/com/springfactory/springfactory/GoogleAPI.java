package com.springfactory.springfactory;

import org.springframework.stereotype.Component;

@Component
public class GoogleAPI extends APIDetect{

    private static final String uniqueName="GOOGLE-API";
    private static final GoogleAPI INSTANCE = new GoogleAPI();
    private GoogleAPI(){/*This construction can never be called outside this class*/}

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

    public static GoogleAPI getInstance(){
        return INSTANCE;
    }

}

// ALL CALLS WILL RETURN SAME INSTANCE!
// Uses Eager Initalization: Booting is slow, but perf is fast
// GoogleAPI api = GoogleAPI.getInstance();
