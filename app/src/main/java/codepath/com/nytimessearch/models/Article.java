package codepath.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;


@Parcel
public class Article {

    public enum Type {
        WITH_IMAGE(0),
        NO_IMAGE(1);

        public final int value;

        Type(int value) {
            this.value = value;
        }
    }

    @SerializedName("web_url")
    String webUrl;
    @SerializedName("headline")
    Headline headline;
    @SerializedName("multimedia")
    List<Multimedia> multimedia;
    @SerializedName("snippet")
    String snippet;
    @SerializedName("section_name")
    String newsDesk;
    @SerializedName("_id")
    String id;
    @SerializedName("pub_date")
    String pubDate;


    public Article() {

    }

    public String getHeadline() {
        return headline.getMain();
    }

    public String getPhoto() {
        for (Multimedia media : multimedia) {
            if (media.getSubtype().equals("xlarge")) {
                return media.getUrl();
            }
        }
        return "";
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public String getPubDate() {
        return pubDate;
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
