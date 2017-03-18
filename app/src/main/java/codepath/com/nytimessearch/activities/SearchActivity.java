package codepath.com.nytimessearch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.ArrayList;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.adapters.ArticleAdapter;
import codepath.com.nytimessearch.fragments.FilterSearchDialog;
import codepath.com.nytimessearch.models.Article;
import codepath.com.nytimessearch.models.FilterData;
import codepath.com.nytimessearch.models.NYTimesResponse;
import codepath.com.nytimessearch.network.NYTHttpClient;
import codepath.com.nytimessearch.utils.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;


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

        filter = (savedInstanceState != null) ? (FilterData) Parcels.unwrap(savedInstanceState.getParcelable(FILTER_VALUE)) : new FilterData();


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
        setupToolbar();

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
        outState.putParcelable(FILTER_VALUE, Parcels.wrap(filter));
        super.onSaveInstanceState(outState);
    }

    private void newSearch() {
        articles.clear();
        // adapter.notifyDataSetChanged();
        searchArticles(0);
        scrollListener.resetState();
    }

    private void searchArticles(int page) {
        Callback callback = new Callback<NYTimesResponse>() {
            @Override
            public void onFailure(Call<NYTimesResponse> call, Throwable t) {
                t.printStackTrace();

            }

            @Override
            public void onResponse(Call<NYTimesResponse> call, retrofit2.Response<NYTimesResponse> response) {
                // handle response here
                NYTimesResponse nyTimesResponse = response.body();
                if (nyTimesResponse != null && nyTimesResponse.getResponse() != null) {
                    articles.addAll(nyTimesResponse.getResponse().getArticles());
                    adapter.notifyDataSetChanged();
                }

            }
        };

        NYTHttpClient.searchArticles(filter, page, callback, this);
    }

    @Override
    public void filterResults(FilterData filter) {
        this.filter = filter;
        newSearch();
        setTitle();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle();

    }

    private void setTitle() {
        String title = filter.isArts() ? "Arts" : "".concat(filter.isFashion() ? " Fashion" : "")
                .concat(filter.isSports() ? " Sports" : "").trim();
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }

    }
}
