package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads Article list by using AsyncTask to perform network request to given URL
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    /** Query URL */
    private String mUrl;

    /**
     * Constructs new {@link ArticleLoader}.
     * @param context of activity
     * @param url to load data
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /* Background thread */
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        /* Perform network request, parse response, and extract list of articles */
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
