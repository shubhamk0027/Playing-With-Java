import java.util.HashMap;

public class APIFactory {
    private static HashMap<String,APIInterface> nameToAPI=new HashMap<>();

    private APIFactory() throws Exception {
        /*Prevent from creating any Instance outside the class*/
        GoogleAPI.register();
        TwitterAPI.register();
        RedditAPI.register();
    }

    public static APIInterface getAPI(String name) throws Exception {
        if(name==null || !nameToAPI.containsKey(name)){
            throw new Exception("No Such API!");
        }
        return nameToAPI.get(name);
    }

    synchronized public static void registerAPI(APIInterface newAPI) throws Exception{
        if(nameToAPI.containsKey(newAPI.name())){
            throw new Exception("API Already Registered!");
        }
        nameToAPI.put(newAPI.name(),newAPI);
    }

    // A synchronized instance method in Java is synchronized on the instance (object)
    synchronized public static void deRegisterAPI(String name) throws Exception {
        if(!nameToAPI.containsKey(name)){
            throw new Exception("API Not Registered!");
        }
        nameToAPI.remove(name);
    }

    public static void main(String[] args) {
        Thread T1= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Thread 2");
                    GoogleAPI.register();
                    GoogleAPI.deRegister();
                    TwitterAPI.register();
                    TwitterAPI.deRegister();
                    RedditAPI.register();
                    RedditAPI.deRegister();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread T2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Thread 1");
                    GoogleAPI.register();
                    GoogleAPI.deRegister();
                    TwitterAPI.register();
                    TwitterAPI.deRegister();
                    RedditAPI.register();
                    RedditAPI.deRegister();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        T1.start();
        T2.start();
        System.out.println("Running App");
    }
}
