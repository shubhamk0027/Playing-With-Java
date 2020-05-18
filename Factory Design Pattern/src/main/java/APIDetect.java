public abstract class APIDetect implements APIInterface {

    public abstract String name();
    public abstract String detect(String Text) throws Exception;

    public Boolean Validate(String text) {
        // Exception will not be thrown here!
        if (text==null) { throw new NullPointerException("Text Can Not Be Null");}
        /* Logic To Validate The Text Common To All The APIS*/
        return Boolean.TRUE;
    }

}
