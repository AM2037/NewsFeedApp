package com.example.android.newsfeedapp;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving article data from The Guardian.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Article} objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Article> myNews = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Article}s
        return myNews;
    }



    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String jsonResponse) {
        /*String title;
        String category;
        String author;
        String datePosted;
        String urlSource;*/

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        List<Article> myNews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);

            // Extract the JSONObject associated with the key called "response",
            // which represents a list of responses (or articles).
            JSONObject responseResult = baseJsonResponse.getJSONObject("response");

            // Extract JSONArray "results" which represents a list of articles
            JSONArray articleArray = responseResult.getJSONArray("results");

            // For each article in the articleArray, create an {@link Article} object
            for (int i = 0; i < articleArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = articleArray.getJSONObject(i);

                /*title = currentArticle.getString("webTitle");
                urlSource = currentArticle.getString("webUrl");
                date = currentArticle.getString("webPublicationDate");
                THESE ARE THE ONES BAILEY USED IN VIDEO*/

                // For a given article, extract the JSONObject associated with the
                // key called "fields", which represents a list of all fields
                // for that article (i.e. thumbnail url, headline, etc.)
                JSONObject fields = currentArticle.getJSONObject("fields");

                // Extract the JSONArray with key "tags" to get author, etc.

                JSONArray authorArray = currentArticle.getJSONArray("tags");
                // Extract the value for the key called "contributor"; author = contributor
                String author = currentArticle.getString("contributor");
                Log.i(LOG_TAG, "Author's name: " + author);

                //Concatenation of author name and type of author (pulled from JSON)
                StringBuilder authorBuilder = new StringBuilder();
                authorBuilder.append(author);

                //Check for 2nd author from @TheBaileyBrew github
                if (authorArray.length() > 1 ){
                    JSONObject secondaryAuthor = authorArray.getJSONObject(1);
                    String secondAuthor = secondaryAuthor.getString("webTitle");
                    authorBuilder.append(" & ");
                    authorBuilder.append(secondAuthor);
                }
                author = authorBuilder.toString();

                // If mapping value = null or the array has at least one element
                if (!currentArticle.isNull("tags") && authorArray.length() > 0) {
                    // get 1st object
                    JSONObject objectTag = (JSONObject) authorArray.get(0);
                    Log.i(LOG_TAG, "TAG Object: " + objectTag);
                } else {
                    // missing info on author
                    author = "Missing contributor(s) info";
                }

                // Extract the value for the key called "webTitle"; title = headline
                String title = currentArticle.getString("webTitle");
                Log.i(LOG_TAG, "Article title: " + title);


                /*Extract the value for the key called "thumbnail"; image = imageResourceId
                String image = fields.getString("thumbnail");
                Log.i(LOG_TAG, "Article Image URL: " + image);*/

                // Extract the value for the key called "sectionName"; category = topic
                String category = currentArticle.getString("sectionName");
                Log.i(LOG_TAG, "Section name: " + category);


                // Extract the value for the key called "webPublicationDate"; published = date
                String published = currentArticle.getString("webPublicationDate");

                // Format date
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(published);
                String formatDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
                published = formatDate;
                Log.i(LOG_TAG, "Date published: " + published);


                // Extract the value for the key called "webUrl"; address = url
                String address = currentArticle.getString("webUrl");
                Log.i(LOG_TAG, "Article URL: " + address);


                // Create a new {@link Article} object with the headline, topic
                // contributor, date, and url from the JSON response.
                Article article = new Article(title, category, author, published, address);

                // Add the new {@link Article} to the list of articles.
                myNews.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return myNews;
    }
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        // Create connection
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        //return jsonResponse;
        return output.toString();
    }
}