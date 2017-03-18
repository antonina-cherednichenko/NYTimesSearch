package codepath.com.nytimessearch.network;


import codepath.com.nytimessearch.models.NYTimesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTimesArticleService {


    @GET("articlesearch.json")
    Call<NYTimesResponse> listArticles(@Query("page") Integer page, @Query("q") String query,
                                       @Query("begin_date") String date, @Query("sort") String sort,
                                       @Query("fq") String newsDesk);


}
