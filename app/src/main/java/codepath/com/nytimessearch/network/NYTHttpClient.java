package codepath.com.nytimessearch.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.FilterData;
import codepath.com.nytimessearch.models.NYTimesResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tonya on 3/16/17.
 */

public class NYTHttpClient {

    private final static String API_KEY = "b5c30576eb72418db66bab0c6714a2f9";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private final static String url = "https://api.nytimes.com/svc/search/v2/";

    private final static NYTimesArticleService service;

    //Initialize okhttp client, retrofit and nytimes service
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url1 = originalHttpUrl.newBuilder()
                    .addQueryParameter("api-key", API_KEY)
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url1);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NYTimesArticleService.class);
    }


    public static void searchArticles(FilterData filter, int page, Callback callback, Context context) {
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


        //Set calendar begin date if available
        String date = filter.getCal() != null ? sdf.format(filter.getCal().getTime()) : null;


        //Set sort order if available
        String sort = TextUtils.isEmpty(filter.getOrder()) ? null : filter.getOrder();


        //Set query if available
        if (!TextUtils.isEmpty(filter.getQuery())) {


        }
        String query = TextUtils.isEmpty(filter.getQuery()) ? null : filter.getQuery();


        //Set topics if available
        String newsDesk = null;
        if (filter.isSports() || filter.isFashion() || filter.isArts()) {
            newsDesk = String.format("news_desk:(%s%s%s)", filter.isArts() ? "\"Arts\"" : "",
                    filter.isFashion() ? " \"Fashion \\u0026 Style\"" : "",
                    filter.isSports() ? " \"Sports\"" : "");
        }

        //Call http client for response
        Call<NYTimesResponse> call = service.listArticles(page, query, date, sort, newsDesk);
        call.enqueue(callback);
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
