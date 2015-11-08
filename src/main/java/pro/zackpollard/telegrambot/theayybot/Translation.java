package pro.zackpollard.telegrambot.theayybot;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * @author Zack Pollard
 */
public class Translation {

    private static final String DETECT_URL = "https://translate.yandex.net/api/v1.5/tr.json/detect";
    private static final String TRANSLATE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    public static String detectLanguage(String text) {

        try {

            JSONObject response = Unirest.post(DETECT_URL)
                    .field("key", Main.YANDEX_API_KEY)
                    .field("text", text)
                    .asJson().getBody().getObject();

            if(response.getInt("code") == 200) {

                return response.getString("lang");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String translateText(String text, String to) {

        try {
            JSONObject response = Unirest.post(TRANSLATE_URL)
                    .field("key", Main.YANDEX_API_KEY)
                    .field("text", text)
                    .field("lang", to)
                    .asJson().getBody().getObject();

            if(response.getInt("code") == 200) {

                return response.getJSONArray("text").getString(0);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }
}
