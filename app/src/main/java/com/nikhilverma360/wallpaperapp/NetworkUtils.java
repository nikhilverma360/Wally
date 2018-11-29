package com.nikhilverma360.wallpaperapp;


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

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public NetworkUtils() {
        super();
    }

    public static List<Image> fetchImagesData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem While making HTTP Request", e);
        }
        return extractDataFromJson(jsonResponse);

    }



    public static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem requesting url", e);
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
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code" + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }

        finally {
            if (urlConnection!=null) {
                urlConnection.disconnect();
            }

            if (inputStream!=null) {
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


    private static List<Image> extractDataFromJson(String imageJSON)  {
        // If the JSON String is empty or null, then return early.
        if (TextUtils.isEmpty(imageJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to.
        List<Image> imageList = new ArrayList<>();


        try {

           // JSONArray rootJsonArray = new JSONArray(imageJSON);
            JSONObject rootJsonObject = new JSONObject(imageJSON);
            JSONArray hitsJsonArray = rootJsonObject.getJSONArray("hits");

            for (int i=0; i<hitsJsonArray.length(); i++) {

                JSONObject currentJsonObject = hitsJsonArray.getJSONObject(i);

                String previewUrl = currentJsonObject.getString("previewURL");
                String webFormatUrl = currentJsonObject.getString("webformatURL");
                String largeImageUrl = currentJsonObject.getString("largeImageURL");

                Image images = new Image(previewUrl, webFormatUrl, largeImageUrl);
                imageList.add(images);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return imageList;


    }

}
