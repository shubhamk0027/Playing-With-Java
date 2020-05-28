package com.api.newsriver;

import ch.qos.logback.core.pattern.color.BlueCompositeConverter;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

public class APIQuery {

    public enum SortBy {
        SCORE("_score"),
        DISCOVER_DATE("discoverDate"),
        READ_TIME("metadata.readTime.seconds");
        private String exp;
        SortBy(String exp){ this.exp=exp; }
        public String getVal(){return exp;}
    };

    public enum SortOrder{
        DSC("DESC"),
        ASC("ASC");
        private String exp;
        SortOrder(String exp){ this.exp= exp; }
        public String getVal(){ return exp; }
    }


    private String luceneQuery;
    private int limit;
    private SortOrder sortOrder;
    private SortBy sortBy;

    public APIQuery(String luceneQuery, SortBy sortBy, SortOrder sortOrder, int limit){
            this.luceneQuery=luceneQuery;
            this.limit=limit;
            this.sortBy=sortBy;
            this.sortOrder=sortOrder;
    }

    public String getLuceneQuery() { return luceneQuery;}
    public String getSortBy() { return sortBy.exp;}
    public String getSortOrder() { return sortOrder.exp;}
    public String getLimit() { return Integer.toString(limit); }
    public void setLuceneQuery(String query){ this.luceneQuery=query;}

    // THE BUILDER PATTERN FOR SETTING UP THE VARIABLES
    public static class Builder{

        // DEFAULT PARAMETERS
        private String luceneQuery;
        SortBy sortBy;
        SortOrder sortOrder;
        private int limit;

        Builder(){
            this.luceneQuery= "*";
            this.sortBy= SortBy.DISCOVER_DATE;
            this.sortOrder=SortOrder.DSC;
            this.limit=5;
        }

        public APIQuery build(){ return new APIQuery(luceneQuery,sortBy,sortOrder,limit);}
        public Builder setQuery(String query){ this.luceneQuery=query; return this;}
        public Builder setSortBy(SortBy sortBy){ this.sortBy=sortBy; return this;}
        public Builder setSortOrder(SortOrder sortOrder){ this.sortOrder=sortOrder; return this;}
        public Builder limit(int limit){ this.limit=limit; return this;}
    }

}
