package com.example.android.newsfeedapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {
    private String LOG_TAG = ArticleAdapter.class.getSimpleName();

    /**
     * Own custom constructor used to inflate layout, and populate lists with data
     * @param context is the current context used to inflate the layout
     * @param articles is a list of Article objects we are displaying to the user
     */

    public ArticleAdapter(Activity context, ArrayList<Article> articles) {

        /* Need to initialize storage for the list. Since 2nd argument is for one TextView
          say 0 to use own adapter for multiple TextViews.
        */
        super(context, 0, articles);
    }

    /**
     * View for AdapterView (ListView)
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        /* Check for repeating view */
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        /* Get {@link Article} object located here on the list */
        final Article currentArticle = getItem(position);
        Log.i(LOG_TAG, "The item is positioned at " + position);


        /* Find TextView in the list_item.xml layout with the ID article_topic */
        TextView topicTextView = listItemView.findViewById(R.id.article_topic);
        assert currentArticle != null;
        topicTextView.setText(currentArticle.getTopic());

        /* Find ImageView in list_item.xml layout with ID article_img */
        Bitmap articlePhoto = currentArticle.getThumbnail();
        ImageView bitmapView = listItemView.findViewById(R.id.article_img);
        bitmapView.setImageBitmap(articlePhoto);

        /* Find TextView in the list_item.xml layout with ID article_title */
        TextView headlineTextView = listItemView.findViewById(R.id.article_title);
        headlineTextView.setText(currentArticle.getHeadline());

        /* Find TextView in list_item.xml with ID article_paragraph */
        TextView paragraphTextView = listItemView.findViewById(R.id.paragraph_text);
        paragraphTextView.setText(currentArticle.getParagraph());

        String articleAuthor = "By " + currentArticle.getContributor() + " ";
        /* Find TextView in list_item.xml layout with ID article_contributor */
        TextView contributorTextVIew = listItemView.findViewById(R.id.article_contributor);
        contributorTextVIew.setText(articleAuthor);


        /* Find TextView in list_item.xml with ID article_date */
        TextView dateTextView = listItemView.findViewById(R.id.article_date);
        dateTextView.setText(currentArticle.getDate());

        return listItemView;
    }
}

