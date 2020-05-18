import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestRules {

    @BeforeClass
    public static void beforeClass(){
        System.out.println(".....This is called once Before the class");
    }

    @Before
    public void before(){
        System.out.println(".....This is Called before executing all the tests");
        System.out.println(".....And will do stuffs like adding/removing file before/after debugging");
    }

    @After
    public void after(){
        System.out.println(".....This is called after the tests");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println(".....This is called once After the class");
    }

    @Rule
    public TestRule listen = new TestWatcher(){

        @Override
        public void failed(Throwable t, Description desc){
            System.out.println("Test "+desc.getMethodName()+ "Failed");
        }

        @Override
        public void succeeded(Description desc){
            System.out.println("Test "+desc.getMethodName()+ "Passed");
        }

    };
}
