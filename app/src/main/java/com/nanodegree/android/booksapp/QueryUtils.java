package com.nanodegree.android.booksapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by jdifuntorum on 6/18/17.
 * Helper Methods that request all the info from Google Books API
 */

public final class QueryUtils {

    /** Keys for JSON parsing */
    private static final String KEY_VOLUMEINFO = "volumeInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PAGECOUNT = "pageCount";
    private static final String KEY_IMAGELINKS = "imageLinks";
    private static final String KEY_SMALLTHUMBNAIL = "smallThumbnail";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_PREVIEWLINK = "previewLink";

    public static String errorMSG = null;

    /** Tag for log message */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){
    }

    /*
    Query Google Books data set and return {@link Event} object to represent the data
     */
    public static ArrayList<BookInfo> fetchBooksData(String requestURL) {

        //Create URL object
        URL url = createUrl(requestURL);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        ArrayList<BookInfo> bookInfos = extractBookData(jsonResponse);

        //return object as result
        return bookInfos;
    }

    private static URL createUrl (String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }


    // make an HTTP request to given URL and return String as response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(1000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /* millseconds */);
            urlConnection.connect();

            //Success = response code 200, read and parse response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Convert InputStream into a String which contains JSON response from server
    private static String readFromStream (InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<BookInfo> extractBookData(String bookDataJSON) {
        // If the JSON string is empty or null return early.
        if (TextUtils.isEmpty(bookDataJSON)) {
            return null;
        }

        // Create empty ArrayList in which the parsed data will be added
        ArrayList<BookInfo> bookInfos = new ArrayList<BookInfo>();

        // Create try/catch block to throw any errors when parsing JSONObject.
        try {
            // Build a list of BookItem Objects
            JSONObject baseJSONObject = new JSONObject(bookDataJSON);
            JSONArray bookArray = baseJSONObject.getJSONArray("items");


            //Extract out the title all relevant values for books
            // Variables for JSON parsing
            String title;
            JSONArray authors;
            String author;
            String description;
            int pageCount;
            JSONObject imageLinks;
            String smallThumbnail;
            String language;
            String previewLink;

            //for loop will check all JSON keys for a value and return them if present, null if not.
            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject(KEY_VOLUMEINFO);
                if (volumeInfo.has(KEY_TITLE)) {
                    title = volumeInfo.getString(KEY_TITLE);
                } else {
                    title = null;
                }
                if (volumeInfo.has(KEY_AUTHORS)) {
                    authors = volumeInfo.getJSONArray(KEY_AUTHORS);
                    author = authors.getString(0);
                } else {
                    author = null;
                }

                if (volumeInfo.has(KEY_DESCRIPTION)) {
                    description = volumeInfo.getString(KEY_DESCRIPTION);
                } else {
                    description = null;
                }

                if (volumeInfo.has(KEY_PAGECOUNT)) {
                    pageCount = volumeInfo.getInt(KEY_PAGECOUNT);
                } else {
                    pageCount = 0;
                }

                if (volumeInfo.has(KEY_IMAGELINKS)) {
                    imageLinks = volumeInfo.getJSONObject(KEY_IMAGELINKS);
                    smallThumbnail = imageLinks.getString(KEY_SMALLTHUMBNAIL);
                } else {
                    smallThumbnail = null;
                }

                if (volumeInfo.has(KEY_LANGUAGE)) {
                    language = volumeInfo.getString(KEY_LANGUAGE);
                } else {
                    language = null;
                }

                if (volumeInfo.has(KEY_PREVIEWLINK)) {
                    previewLink = volumeInfo.getString(KEY_PREVIEWLINK);
                } else {
                    previewLink = null;
                }

                // Create the BookItem object and add it to the ArrayList, mind the order of parameters defined in BookInfo Class.
                BookInfo bookInfo = new BookInfo(author, smallThumbnail, previewLink, title, pageCount,
                        language, description);
                bookInfos.add(bookInfo);
            }
        } catch (JSONException e){
            Log.e(LOG_TAG, "Issue: JSON results parsing error @QueryUtils", e);
            errorMSG = "JSON results parsing error. JSONExection: " + e;
        }
        return bookInfos;
    }

}
