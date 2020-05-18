import org.junit.runner.JUnitCore;

public class main {
    /*
    No Setup.. Needed
    Needed to change the testCompile in Gradle to compile
    Otherwise will not be able to import!
     */

    /*
    Test Cases also can be developed from a json file
    see - https://www.youtube.com/watch?v=fVfR3pO-5V4
     */

    /*
    Annotations In Java:
    Annotations are used to provide supplement information about a program.
        -Kind a Syntactic metadata
        -start with ‘@’.
        -do not change action of a compiled program.
        -help to associate metadata (information) to the program elements i.e. instance variables, constructors, methods, classes, etc.
        -not pure comments as they can change the way a program is treated by compiler. Example.
            Ex extra override will cause error!
     */

    public static void main(String args[]){
        System.out.println("Running Classes");
//        JUnitCore.runClasses(TestPrimer.class);
    }
}
