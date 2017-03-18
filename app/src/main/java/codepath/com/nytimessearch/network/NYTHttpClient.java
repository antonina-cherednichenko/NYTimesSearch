package codepath.com.nytimessearch.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.text.SimpleDateFormat;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.FilterData;

/**
 * Created by tonya on 3/16/17.
 */

public class NYTHttpClient {

    private final static String API_KEY = "b5c30576eb72418db66bab0c6714a2f9";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private final static AsyncHttpClient client = new AsyncHttpClient();
    private final static String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    public static void searchArticles(FilterData filter, int page, JsonHttpResponseHandler handler, Context context) {
        if (!isNetworkAvailable(context)) {
            Snackbar.make(((Activity) context).findViewById(R.id.gvResults),
                    context.getResources().getString(R.string.network_is_not_available), Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!isOnline()) {
            Snackbar.make(((Activity) context).findViewById(R.id.gvResults),
                    context.getResources().getString(R.string.internet_is_not_available), Snackbar.LENGTH_LONG).show();
            return;

        }

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);


        //Set calendar begin date if available
        if (filter.getCal() != null) {
            String date = sdf.format(filter.getCal().getTime());
            params.put("begin_date", date);
        }

        //Set sort order if available
        if (!TextUtils.isEmpty(filter.getOrder())) {
            params.put("sort", filter.getOrder());
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

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


}
