import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class RedditAPITest {

    @Test
    public void registrationTest() throws Exception {
        RedditAPI.register();
        APIInterface api = APIFactory.getAPI("REDDIT-API");
        RedditAPI.deRegister();

        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"REDDIT-API");
    }

    @Test (expected = NullPointerException.class)
    public void nullDetectTest() throws Exception{
        RedditAPI redditAPI= RedditAPI.getInstance();
        redditAPI.detect(null);
    }
}

