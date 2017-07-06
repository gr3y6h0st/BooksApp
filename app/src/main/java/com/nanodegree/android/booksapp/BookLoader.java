package com.nanodegree.android.booksapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.ArrayList;

/**
 * Created by jdifuntorum on 6/19/17.
 */

public class BookLoader extends AsyncTaskLoader<ArrayList<BookInfo>> {

    private static final String LOG_TAG = BookLoader.class.getName();

    // Query URL
    private String mUrl;

    /*
    Constructs a loader {@link BookLoader}.
     */

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<BookInfo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<BookInfo> bookInfos = QueryUtils.fetchBooksData(mUrl);
        return bookInfos;
    }
}
