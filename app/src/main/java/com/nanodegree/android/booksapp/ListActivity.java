package com.nanodegree.android.booksapp;

/**
 * Created by jdifuntorum on 6/24/17.
 */

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements
        LoaderCallbacks<ArrayList<BookInfo>> {

    //Adapter list variable
    private BookInfoAdapter bookAdapter;

    private static final int BOOK_LOADER_ID = 1;

    //Log Tag
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //empty view
    private TextView mEmptyTextView;

    private ProgressBar progressBar;

    //Preference: results defined per user
    private String maxResults;

    // User input
    private String authorQuery;
    private String titleQuery;


    //declare final version of API Query as empty for now
    private String finalQuery;

    //Initial Query
    private static final String API_INITIAL_QUERY = "https://www.googleapis" + ".com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //read inputs
        maxResults = MainActivity.spinnerSelection;
        titleQuery = MainActivity.editTextTitleInput;
        authorQuery = MainActivity.editTextAuthorInput;


        //returnFinalQuery is a method that combines strings together (takes three strings as parameters).
        //Pass user input as the parameters to create the finalQuery.
        finalQuery = returnFinalQuery(titleQuery, authorQuery, maxResults);

        // Find the listview by layout ID
        ListView listView = (ListView) findViewById(R.id.list);

        // Find the empty View by layout ID
        mEmptyTextView = (TextView) findViewById(R.id.empty_TextView);

        // global bookAdapter variable receives ArrayList input, set into listView
        bookAdapter = new BookInfoAdapter(this, new ArrayList<BookInfo>());
        listView.setAdapter(bookAdapter);

        //when user clicks on listing for book, open browser to show them a preview link.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get position of book item
                BookInfo bookDetail = bookAdapter.getItem(position);

                // Converts String Url for Preview URLfound in book detail  --> as a URI object.
                Uri bookURL = Uri.parse(bookDetail.getmPreviewURL());

                // Pass URI object created above into the Intent and start.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookURL);
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check network, fetch data if connected and reference to loader manager
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // No conncetion = No progressBar needed, hide it.
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // Notify users via error message that Network Connection is not present.
            mEmptyTextView.setText(R.string.no_internet);
        }
    }

    public String returnFinalQuery(String titleInput, String authorInput, String maxResults) {

        String[] titleText = titleInput.split("\\s+");
        String titleTextQuery = null;

        // eliminate spaces using for loop. Loop titleText parameter and obtain the words.
        for (int i = 0; i < titleText.length; i++) {
            if (i == 0) {
                titleTextQuery = titleText[i];
            } else {
                titleTextQuery = titleTextQuery + "+" + titleText[i];
            }
        }
        Log.v("BookListActivity", "Title: " + titleTextQuery);

        // If the title is not empty, add parameters as per API
        if (TextUtils.isEmpty(titleTextQuery)) {
            titleTextQuery = "";
        } else {
            titleTextQuery = "intitle:" + titleTextQuery;
        }

        String[] authorTextInput = authorInput.split("\\s+");
        String authorTextQuery = null;

        for (int i = 0; i < authorTextInput.length; i++) {
            if (i == 0) {
                authorTextQuery = authorTextInput[i];
            } else {
                authorTextQuery = authorTextQuery + "+" + authorTextInput[i];
            }
        }
        // If the author is not empty, add parameters as per API
        if (TextUtils.isEmpty(authorTextQuery)) {
            authorTextQuery = "";
        } else if (TextUtils.isEmpty(titleTextQuery)) {
            authorTextQuery = "inauthor:" + authorTextQuery;
        } else {
            authorTextQuery = "&inauthor:" + authorTextQuery;
        }

        //Concat all queries into a master query
        finalQuery = API_INITIAL_QUERY + titleTextQuery + authorTextQuery + "&maxResults=" + maxResults;

        return finalQuery;
    }

    @Override
    public Loader<ArrayList<BookInfo>> onCreateLoader(int id, Bundle args) {
        // Create new loader for the given URL
        return new BookLoader(this, finalQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BookInfo>> loader, ArrayList<BookInfo> bookInfo) {
        // Clear adapter
        bookAdapter.clear();
        if (QueryUtils.errorMSG != null) {
            Toast.makeText(MainActivity.mContext, QueryUtils.errorMSG, Toast.LENGTH_SHORT).show();
            QueryUtils.errorMSG = null;
        }

        // Validate {@link BookInfo} data, bookAdapter adds data to listView.
        if (bookInfo != null && !bookInfo.isEmpty()) {
            bookAdapter.addAll(bookInfo);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

        } else {
            //No results message if book data is invalid or empty
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            mEmptyTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BookInfo>> loader) {
        bookAdapter.clear();
    }
}
