package com.nanodegree.android.booksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jdifuntorum on 6/19/17.
 */

public class BookInfoAdapter extends ArrayAdapter<BookInfo> {
    Context mContext;

    /**
     * @param context   = Current Activity
     * @param bookInfos = list of {@link BookInfo}s displayed.
     */

    public BookInfoAdapter(Context context, ArrayList<BookInfo> bookInfos) {
        super(context, 0, bookInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View bookInfoView = convertView;
        if (bookInfoView == null) {
            bookInfoView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }
        BookInfo bookDetail = getItem(position);

        ImageView thumbnailView = (ImageView) bookInfoView.findViewById(R.id.thumbnail_view);

        if (bookDetail.getmSmallThumbURL() != null) {
            Picasso.with(getContext()).load(bookDetail.getmSmallThumbURL()).into(thumbnailView);
        } else {
            thumbnailView.setImageResource(R.drawable.no_image);
        }

        // Get the descript from the currentBookItem object and set this text on the TextView.
        TextView descriptionTextView = (TextView) bookInfoView.findViewById(R.id.description_view);
        descriptionTextView.setText(bookDetail.getmSynopsis());

        TextView authorTextView = (TextView) bookInfoView.findViewById(R.id.author_view);
        // Get the Author
        authorTextView.setText(bookDetail.getmAuthor());

        TextView languageTextView = (TextView) bookInfoView.findViewById(R.id.language_view);
        // Get the Language
        languageTextView.setText(bookDetail.getmLanguage());

        TextView pagesTextView = (TextView) bookInfoView.findViewById(R.id.pages_view);
        // Get the # of pages
        pagesTextView.setText(bookDetail.getmPages());

        TextView titleTextView = (TextView) bookInfoView.findViewById(R.id.title_view);
        // Get Title
        titleTextView.setText(bookDetail.getmTitle());

        return bookInfoView;
    }

}
