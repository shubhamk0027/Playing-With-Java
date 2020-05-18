
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class IntegrationTest {

    @Test(expected = Exception.class)
    public void registrationDregistrationTest() throws Exception {

        GoogleAPI.register();
        TwitterAPI.register();
        RedditAPI.register();

        assertEquals(APIFactory.getAPI("GOOGLE-API").name(),"GOOGLE-API");
        assertEquals(APIFactory.getAPI("TWITTER-API").name(),"TWITTER-API");
        assertEquals(APIFactory.getAPI("REDDIT-API").name(),"REDDIT-API");

        GoogleAPI.deRegister();
        APIFactory.getAPI("GOOGLE-API").name();
    }

}



