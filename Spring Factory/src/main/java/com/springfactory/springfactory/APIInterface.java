package com.springfactory.springfactory;

public interface APIInterface {

    String detect(String Text) throws Exception;
    // Method to detect the type of API to be called
    String name();
    // Method returning the API signature/unique name
}

/*
Structure ->
    public class APIInterface
    -public abstract class APIDetect implements APIInterface
    --public class GoogleAPI [Singleton]
    --public class TwitterAPI [Singleton]
    --public class RedditAPI [Singleton]
    public class APIFactory [Singleton]
 */