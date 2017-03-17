package codepath.com.nytimessearch.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.adapters.ArticleAdapter;
import codepath.com.nytimessearch.models.Article;
import codepath.com.nytimessearch.network.NYTHttpClient;
import codepath.com.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity {
    RecyclerView gvResults;

    private ArrayList<Article> articles;
    private ArticleAdapter adapter;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private static final String QUERY_VALUE = "query";


    private static String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY_VALUE);
        }

    }

    private void setupViews() {
        gvResults = (RecyclerView) findViewById(R.id.gvResults);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gvResults.setLayoutManager(gridLayoutManager);
        gvResults.setAdapter(adapter);
        newSearch(query);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        gvResults.addOnScrollListener(scrollListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                newSearch(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Write your code here
                showAllArticles();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY_VALUE, query);
        super.onSaveInstanceState(outState);
    }

    private void loadNextDataFromApi(int offset) {
        searchArticles(query, offset);
    }

    private void showAllArticles() {
        newSearch("");
    }

    private void newSearch(String sq) {
        articles.clear();
        query = sq;
        searchArticles(sq, 0);
    }

    private void searchArticles(String query, int page) {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("DEBUG", errorResponse.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJsonArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NYTHttpClient.searchArticles(query, page, handler);
    }

}
