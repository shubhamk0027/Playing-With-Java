
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GoogleAPITest.class,
        TwitterAPITest.class,
        RedditAPITest.class,
})
public class UnitTestPrimer {
}