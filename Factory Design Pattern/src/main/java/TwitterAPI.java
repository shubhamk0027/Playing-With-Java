import java.beans.VetoableChangeListener;

public class TwitterAPI extends APIDetect{

    private static final String uniqueName="TWITTER-API"; /*Same Instance of String returned*/
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

    public static void register() throws Exception {
        APIFactory.registerAPI(INSTANCE);
        System.out.println("Twitter Api Registered");
    }

    public static void deRegister() throws Exception {
        APIFactory.deRegisterAPI(uniqueName);
        System.out.println("Twitter Api De-Registered");
    }


    /* ALL CALLS WILL RETURN SAME INSTANCE!*/
    public static TwitterAPI getInstance(){ return INSTANCE; }
}
// GoogleAPI api = GoogleAPI.getInstance();
