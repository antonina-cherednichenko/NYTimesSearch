package codepath.com.nytimessearch.network;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by tonya on 3/16/17.
 */

public class NYTHttpClient {

    private final static String API_KEY = "b5c30576eb72418db66bab0c6714a2f9";

    public static void searchArticles(String query, int page, JsonHttpResponseHandler handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);

        if (!TextUtils.isEmpty(query)) {
            params.put("q", query);
        }


        client.get(url, params, handler);
    }


}
