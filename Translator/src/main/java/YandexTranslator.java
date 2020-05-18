import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/*
More about json and parsing:
http://tutorials.jenkov.com/java-json/index.html
https://www.baeldung.com/gson-string-to-jsonobject

Get and Post response in Java
https://mkyong.com/java/how-to-send-http-request-getpost-in-java/
 */

class YandexTranslator {

    private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static String KEY= "GET YOUR OWN KEY";
    private static String mainURL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    public String translate(String text,String fromLang, String toLang) throws Exception {

        String url =  mainURL+
                "?key="+KEY+
                "&text="+URLEncoder.encode(text, StandardCharsets.UTF_8)+
                "&lang="+fromLang+"-"+toLang;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java Tanslator App")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println("Status Code:"+response.statusCode());
        // System.out.println("Response:"+response.body());

        // PARSING THE JSON STRING
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        if(response.statusCode()==200){
            return jsonObject.get("text").getAsString();
        }else{
            return jsonObject.get("message").getAsString();
        }
    }

}
