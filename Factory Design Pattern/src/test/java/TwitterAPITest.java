import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class TwitterAPITest {

    @Test
    public void registrationTest() throws Exception {

        TwitterAPI.register();
        APIInterface api = APIFactory.getAPI("TWITTER-API");
        TwitterAPI.deRegister();

        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"TWITTER-API");
    }

    @Test (expected = NullPointerException.class)
    public void nullDetectTest() throws Exception{
        TwitterAPI twitterAPI= TwitterAPI.getInstance();
        twitterAPI.detect(null);
    }

}

