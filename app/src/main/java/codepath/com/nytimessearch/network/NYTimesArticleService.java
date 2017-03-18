package codepath.com.nytimessearch.network;


import codepath.com.nytimessearch.models.NYTimesResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NYTimesArticleService {

    @GET("articlesearch.json")
    Call<NYTimesResponse> listArticles();


}
