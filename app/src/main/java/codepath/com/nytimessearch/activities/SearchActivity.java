package codepath.com.nytimessearch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import codepath.com.nytimessearch.fragments.FilterSearchDialog;
import codepath.com.nytimessearch.models.Article;
import codepath.com.nytimessearch.models.FilterData;
import codepath.com.nytimessearch.network.NYTHttpClient;
import codepath.com.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import cz.msebera.android.httpclient.Header;


public class SearchActivity extends AppCompatActivity implements FilterSearchDialog.FilteredSearchListener {
    RecyclerView gvResults;

    private ArrayList<Article> articles;
    private ArticleAdapter adapter;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private static final String FILTER_VALUE = "filter";

    private static FilterData filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        articles = new ArrayList<>();
        adapter = new ArticleAdapter(this, articles);

        filter = (savedInstanceState != null) ? (FilterData) savedInstanceState.getSerializable(FILTER_VALUE) : new FilterData();


        gvResults = (RecyclerView) findViewById(R.id.gvResults);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gvResults.setLayoutManager(gridLayoutManager);
        gvResults.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                searchArticles(page);
            }
        };
        gvResults.addOnScrollListener(scrollListener);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Perform initial search
        newSearch();
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
                filter.setQuery(query);
                newSearch();
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
                //show all articles if query is cleared
                filter.clearQuery();
                newSearch();
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                FragmentManager fm = getSupportFragmentManager();
                FilterSearchDialog filterDialog = FilterSearchDialog.newInstance(filter);
                filterDialog.show(fm, "fragment_filter_dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FILTER_VALUE, filter);
        super.onSaveInstanceState(outState);
    }

    private void newSearch() {
        articles.clear();
        // adapter.notifyDataSetChanged();
        searchArticles(0);
    }

    private void searchArticles(int page) {
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

        NYTHttpClient.searchArticles(filter, page, handler);
    }

    @Override
    public void filterResults(FilterData filter) {
        this.filter = filter;
        newSearch();
    }
}
