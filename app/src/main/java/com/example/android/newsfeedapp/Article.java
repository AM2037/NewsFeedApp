package com.example.android.newsfeedapp;


/**
 * {@link Article} represents a single article from the Guardian News Feed.
 * Each object has 6 properties: topic, headline, image, contributor, date, and url.
 */
public class Article {

    // Headline for the article
    private String mHeadline;

    // Topic of the article
    private String mTopic;

    // contributor(s) of the article
    private String mContributor;

    // Date the article was published
    private String mDate;

    // Get URL
    private String mUrl;

    /**
     * Create a new Article object.
     * @param headline is the title of the piece
     * @param topic is the topic or category of the article pulled/queried
     * @param contributor represents the person or contributors who wrote it
     * @param date is when the article was published
     * @param url is the website URL to the article itself
     */
    public Article(String headline, String topic, String contributor, String date, String url) {
        mHeadline = headline;
        mTopic = topic;
        mContributor = contributor;
        mDate = date;
        mUrl = url;
    }

    /**
     * Get title of the article
     */
    public String getHeadline() {
        return mHeadline;
    }


    /**
     * Get topic of the article
     */
    public String getTopic() {
        return mTopic;
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
