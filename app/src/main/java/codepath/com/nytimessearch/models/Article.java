package codepath.com.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tonya on 3/14/17.
 */

public class Article implements Serializable {

    private String webUrl;
    private String headline;
    private String photo;
    private String snippet;

    public String getHeadline() {
        return headline;
    }

    public String getPhoto() {
        return photo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            this.photo = "";
            for (int i = 0; i < multimedia.length(); i++) {
                JSONObject multimediaJson = multimedia.getJSONObject(i);
                if (multimediaJson.getString("subtype").equals("xlarge")) {
                    this.photo = "http://www.nytimes.com/" + multimediaJson.getString("url");
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public enum Type {
        WITH_IMAGE(0),
        NO_IMAGE(1);

        public final int value;

        Type(int value) {
            this.value = value;
        }
    }


    public static ArrayList<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


}
