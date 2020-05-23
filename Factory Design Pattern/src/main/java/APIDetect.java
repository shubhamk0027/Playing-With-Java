public abstract class APIDetect implements APIInterface {

    public abstract String name();
    public abstract String detect(String Text) throws Exception;

    public Boolean Validate(String text) {
        if (text==null) { throw new NullPointerException("Text Can Not Be Null");}
        return Boolean.TRUE;
    }

}
