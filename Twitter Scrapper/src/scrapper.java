import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.List;
import java.util.Map;



public class scrapper {

    static final String CONSUMER_KEY = "GET YOUR OWN KEY";
    static final String CONSUMER_SECRET = "GET YOUR OWN CONSUMER SECRET";
    static final String ACCESS_TOKEN = "GET YOUR OWN ACCESS TOKEN";
    static final String ACCESS_TOKEN_SECRET = "GET YOUR OWN TOKEN SECRET";

    public static Twitter getTwitterInstance(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDaemonEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();
    }

    private static void searchTweets(Twitter twitter, String searchTerm) throws TwitterException{
        System.out.println("\n\nSearch Query: "+searchTerm);
        Query query = new Query(searchTerm);
        QueryResult result = twitter.search(query);
        Integer i=1;
        for(Status status:result.getTweets()){
            System.out.println(i+" @"+status.getUser().getScreenName()+":"+status.getText());
            i=i+1;
        }
    }


    private static void getTimeline(Twitter twitter, String name) throws TwitterException {
        System.out.println("\n\nUser Timeline: "+name);
        Integer i=0;
        List<Status> statuses = twitter.getUserTimeline(name);
        for (Status status : statuses) {
            i=i+1;
            String fmt = "@" + status.getUser().getScreenName() + " - " + status.getText();
            System.out.println(i+" "+fmt);
        }

    }

    public static void main(String [] args) throws TwitterException {
        Twitter twitter = getTwitterInstance();
        searchTweets(twitter,"Apple");
        StackTraceDumper.dumpAllStackTraces();
        getTimeline(twitter,"Apple");
    }
}
