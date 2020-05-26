package com.api.newsriver;
import java.util.ArrayList;
public class APIResponse {
    // for parsing make sure it is public!
    public String id;
    public String discoverDate;
    public String title;
    public String text;
    public String url;
    public Double score;

/*
    static class Element{
        public String type;
        public String primary;
        public String url;
        public String width;
        public String height;
        public String title;
        public String alternative;
    }
    public ArrayList<Element> elements;
*/

/*    static class Website{
        public String name;
        public String hostName;
        public String domainName;
        public String iconUrl;
        public String countryCode;
        public String region;
    }
    public Website website;

    static class Metadata{
        static class ReadTime{
            public String type;
            public Integer seconds;
        }
        public ReadTime readtime;
    }
        public Metadata metadata;
*/
}
