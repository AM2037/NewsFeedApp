package com.example.android.newsfeedapp;

import android.media.Image;

/**
 * {@link Article} represents a single article from the Guardian News Feed.
 * Each object has 6 properties: topic, headline, image, contributor, date, and url.
 */
public class Article {

    // Topic of the article
    private String mTopic;

    /* Image associated with the article
    private String mImageResourceId;
    /**
     * static and final are access modifiers, can never change values aka constant
     * Constant value that represents no image was provided for this word


    private static final int NO_IMAGE_PROVIDED = -1; */

    // Headline for the article
    private String mHeadline;

    // contributor(s) of the article
    private String mContributor;

    // Date the article was published
    private String mDate;

    // Get URL
    private String mUrl;

    /**
     * Create a new Article object.
     * @param topic is the topic or category of the article pulled/queried
     * //@param imageResourceId is the image associated with the article
     * @param headline is the title of the piece
     * @param contributor represents the person or contributors who wrote it
     * @param date is when the article was published
     * @param url is the website URL to the article itself
     */
    public Article(String topic, String headline, String contributor, String date, String url) {
        mTopic = topic;
        //mImageResourceId = imageResourceId;
        mHeadline = headline;
        mContributor = contributor;
        mDate = date;
        mUrl = url;
    }

    /**
     * Get topic of the article
     */
    public String getTopic() {
        return mTopic;
    }

    /**
     * Get image URL for the article


    public String getImageResourceId() {
        return mImageResourceId;
    }

    /*public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }*/


    /**
     * Get title of the article
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * Get authors of the article
     */
    public String getContributor() {
        return mContributor;
    }

    /**
     * Get date of the article
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Get URL of the article
     */
    public String getUrl() {
        return mUrl;
    }

}
