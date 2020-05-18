public class Translator {

    static YandexTranslator yandexTranslator  = new YandexTranslator();

    public static void translate(String text,String langFrom, String langTo) throws Exception {
        System.out.println("Before Translation: "+text);
        String translated = yandexTranslator.translate(text,langFrom,langTo);
        System.out.println("After Translation: "+translated);
    }

    public static void main(String[] args) throws Exception {
        translate("Hello World","en","hi");
        translate("Geeks for Geeks","en","hi");
        translate("Code Forces","en","hi");
        translate("Hacker Rank","en","hi");
        translate("Top Coder","en","hi");
    }
}
