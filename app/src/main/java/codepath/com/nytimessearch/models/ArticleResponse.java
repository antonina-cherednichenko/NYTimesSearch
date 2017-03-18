package codepath.com.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ArticleResponse {

    @SerializedName("docs")
    List<Article> articles;

    public ArticleResponse() {
        articles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return articles;
    }
}
