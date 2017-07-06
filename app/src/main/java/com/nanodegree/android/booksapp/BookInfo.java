package com.nanodegree.android.booksapp;

/**
 * Created by jdifuntorum on 6/19/17.
 * Class will define what information is displayed for the book
 */

public class BookInfo {

    private String mTitle;

    private String mAuthor;

    private String mSynopsis;

    private String mLanugage;

    private String mSmallThumbURL;

    private String mPreviewURL;

    private int mPages;


    public BookInfo(String author, String smallThumbURL, String previewURL, String title, int pages,
                     String language, String synopsis) {
        mAuthor = author;
        mSmallThumbURL = smallThumbURL;
        mPreviewURL = previewURL;
        mTitle = title;
        mPages = pages;
        mSynopsis = synopsis;
        mLanugage = language;

    }

    public String getmPreviewURL(){
        return mPreviewURL;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmTitle(){
        return mTitle;
    }

    public String getmPages(){
        return String.valueOf(mPages);
    }

    public String getmLanguage(){
        return mLanugage;
    }

    public String getmAuthor(){
        return mAuthor;
    }

    public String getmSmallThumbURL(){
        return mSmallThumbURL;
    }
}
