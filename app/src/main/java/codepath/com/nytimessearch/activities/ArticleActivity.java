package codepath.com.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import codepath.com.nytimessearch.R;
import codepath.com.nytimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    public static String ARTICLE_EXTRA = "article";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = (Article) getIntent().getSerializableExtra(ARTICLE_EXTRA);
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }

        });

        webView.loadUrl(article.getWebUrl());


    }

}
