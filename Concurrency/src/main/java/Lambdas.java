interface Algebra {
    int operate(int a, int b);
}
enum Operation {
    ADD, SUB, MUL, DIV
}
/*
Lambda:
Lambdas are similar in function to anonymous classes, but far more concise
Collections.sort(words,(s1, s2) -> Integer.compare(s1.length(), s2.length()));
It doesn't execute on its own and used to implement methods that are declared in a functional interface.
A functional interface is an interface that contains only one abstract method.

 Interfaces with a single abstract method are special and deserve special treatment.
 These interfaces are now known as functional interfaces, and the language allows you to create instances
 of these interfaces using lambda expressions, or lambdas for short.
Collections.sort(words, new Comparator<String>() {
    public int compare(String s1, String s2) {return Integer.compare(s1.length(), s2.length());}
});


set.addObserver(new SetObserver<>() {
    public void added(ObservableSet<Integer> s, Integer e) {
    System.out.println(e);
    if (e == 23)
        s.removeObserver(this);
    }
});

Collections.sort(words, comparingInt(String::length));
words.sort(comparingInt(String::length));

Note that this call uses an anonymous class instance in place of the lambda used in
the previous call.
That is because the function object needs to pass itself to
s.removeObserver,
and lambdas cannot access themselves


 */
public class Lambdas {
        public static void main(String[] args) {
            print((a, b) -> a + b, Operation.ADD); // modified the paramenter!
            print((a, b) -> a - b, Operation.SUB);
            print((a, b) -> a * b, Operation.MUL);
            print((a, b) -> a / b, Operation.DIV);
        }
        static void print(Algebra alg, Operation op) {
            switch (op) {
                case ADD:
                    System.out.println("The addition of a and b is: " + alg.operate(40, 20));
                    break;
                case SUB:
                    System.out.println("The subtraction of a and b is: " + alg.operate(40, 20));
                    break;
                case MUL:
                    System.out.println("The multiplication of a and b is: " + alg.operate(40, 20));
                    break;
                case DIV:
                    System.out.println("The division of a and b is: " + alg.operate(40, 20));
                    break;
                default:
                    throw new AssertionError();
            }
        }
}

