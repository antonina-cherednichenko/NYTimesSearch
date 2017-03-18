package codepath.com.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class Article {

    String webUrl;
    String headline;
    String photo;
    String snippet;
    String newsDesk;
    String id;
    String pubDate;

    public Article() {

    }

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

    public String getPubDate() {
        return pubDate;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");
            this.newsDesk = jsonObject.getString("news_desk");
            this.id = jsonObject.getString("_id");
            this.pubDate = jsonObject.getString("pub_date");

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        return id.equals(article.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
