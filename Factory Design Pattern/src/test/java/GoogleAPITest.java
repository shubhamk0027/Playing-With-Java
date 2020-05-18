import org.junit.Test;
import static junit.framework.TestCase.assertEquals;

public class GoogleAPITest {

    @Test
    public void registrationTest() throws Exception {

        GoogleAPI.register();
        APIInterface api = APIFactory.getAPI("GOOGLE-API");
        GoogleAPI.deRegister();

        assertEquals(api.detect("TEXT"),"DONE");
        assertEquals(api.name(),"GOOGLE-API");
    }

    @Test (expected = NullPointerException.class)
    public void nullDetectTest() throws Exception{
        GoogleAPI googleAPI= GoogleAPI.getInstance();
        googleAPI.detect(null);
    }

}

