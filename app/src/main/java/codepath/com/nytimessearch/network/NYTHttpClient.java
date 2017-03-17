package codepath.com.nytimessearch.network;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;

import codepath.com.nytimessearch.models.FilterData;

/**
 * Created by tonya on 3/16/17.
 */

public class NYTHttpClient {

    private final static String API_KEY = "b5c30576eb72418db66bab0c6714a2f9";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    public static void searchArticles(FilterData filter, int page, JsonHttpResponseHandler handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);


        //Set calendar begin date if available
        if (filter.getCal() != null) {
            String date = sdf.format(filter.getCal().getTime());
            params.put("begin_date", date);
        }

        //Set sort order if available
        if (filter.getOrder() != FilterData.Order.NON_SET) {
            if (filter.getOrder() == FilterData.Order.NEWEST) {
                params.put("sort", "newest");
            } else {
                params.put("sort", "oldest");
            }
        }

        //Set query if available
        if (!TextUtils.isEmpty(filter.getQuery())) {
            params.put("q", filter.getQuery());
        }


        if (filter.isSports() || filter.isFashion() || filter.isArts()) {
            String newsDesk = String.format("news_desk:(%s%s%s)", filter.isArts() ? "\"Arts\"" : "",
                    filter.isFashion() ? " \"Fashion \\u0026 Style\"" : "",
                    filter.isSports() ? " \"Sports\"" : "");
            params.put("fq", newsDesk);
        }


        client.get(url, params, handler);
    }


}
