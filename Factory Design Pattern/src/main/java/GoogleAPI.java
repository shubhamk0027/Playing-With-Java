import java.beans.VetoableChangeListener;

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

    public static void register() throws Exception {
        APIFactory.registerAPI(INSTANCE);
        System.out.println("Google Api Registered");
    }

    public static void deRegister() throws Exception {
        APIFactory.deRegisterAPI(uniqueName);
        System.out.println("Google Api DeRegistered");
    }

    /* ALL CALLS WILL RETURN SAME INSTANCE!*/
    public static GoogleAPI getInstance(){ return INSTANCE; }
}
// GoogleAPI api = GoogleAPI.getInstance();
