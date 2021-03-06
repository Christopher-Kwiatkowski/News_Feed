package com.example.android.newsfeed;

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
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static List<Story> fetchStoryData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Story> stories = extractFeatureFromJson(jsonResponse);
        return stories;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the story JSON results.", e);
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
        return output.toString();
    }

    private static List<Story> extractFeatureFromJson(String storyJSON) {
        if (TextUtils.isEmpty(storyJSON)) {
            return null;
        }

        List<Story> stories = new ArrayList<>();

        try {

            JSONObject baseResponse = new JSONObject(storyJSON);
            JSONObject baseJsonResponse = baseResponse.getJSONObject("response");
            JSONArray storyArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < storyArray.length(); i++) {
                JSONObject currentStory = storyArray.getJSONObject(i);

                String sectionName = currentStory.getString("sectionName");

                String webPublicationDate = "";
                if(currentStory.has("webPublicationDate")) {
                    webPublicationDate = currentStory.getString("webPublicationDate");
                }

                String webTitle = currentStory.getString("webTitle");

                String webUrl = currentStory.getString("webUrl");

                String contributor = "";
                JSONArray tagsArray = currentStory.getJSONArray("tags");

                if((tagsArray.length() != 0) && tagsArray.getJSONObject(0).has("webTitle")) {
                   contributor = tagsArray.getJSONObject(0).getString("webTitle");
                }
                Story story = new Story(sectionName, webPublicationDate, webTitle, webUrl, contributor);
                stories.add(story);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the story JSON results", e);
        }
        return stories;
    }
}
