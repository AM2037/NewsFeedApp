package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?order-by=newest&show-fields=all&api-key=c23cd533-541e-419b-b508-70647adb7468";

    /* Constant value for the article loader ID. Can choose any int (for multiple loaders) */
    private static final int ARTICLE_LOADER_ID = 1;

    /* Adapter for articles list */
    private ArticleAdapter mAdapter;

    /* TextView displayed when list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        /* Find reference to {@link ListView} in layout */
        ListView articleListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);


        /* Create new adapter taking an empty list of articles as input */
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        /* Set the adapter on the {@link ListView} to populate list in UI */
        articleListView.setAdapter(mAdapter);

        /* When clicking on an item it will send users to the article location on The Guardian */
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                // Find current article that's clicked on
                Article currentArticle = mAdapter.getItem(position);

                // Convert String URL to URI object (passed to intent constructor)
                assert currentArticle != null;
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                // Create intent to view URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Launch activity by sending intent
                startActivity(websiteIntent);
            }
        });

        // Get a reference to ConnectivityManager to check state of the network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on currently active default data network
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there's a connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize loader by passing in ID assigned above and pass in null for bundle
            // Pass in this activity for LoaderCallbacks parameter (valid b/c this activity
            // implements the LoaderCallbacks interface)
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message is visible
            View loadingIndicator = findViewById(R.id.progress_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.progress_indicator);
        loadingIndicator.setVisibility(View.GONE);

        /* Set empty state text to display "No articles found." */
        mEmptyStateTextView.setText(R.string.no_articles);

        /* Clear previous data from adapter */
        mAdapter.clear();

        /* Add articles to List if there's a valid one, which will trigger ListView to update */
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            /* Get a reference to ConnectivityManager to check state of the network connectivity */
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            /* Get details on currently active default data network */
            assert connMgr != null;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {

                // Set empty text to display no articles found
                mEmptyStateTextView.setText(R.string.no_articles);
            } else {
                // Update empty state with no connection error
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear existing data from adapter on reset
        mAdapter.clear();
    }

}



