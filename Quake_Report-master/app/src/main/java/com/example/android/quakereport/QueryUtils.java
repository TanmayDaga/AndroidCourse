package com.example.android.quakereport;

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

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * doing all works*/
    public static List<EarthQuake> fetchEarthQuakeData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHTTPRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG,"Problem making http request");
        }
        // Extract features from json;
        return extractFeatureFromJson(jsonResponse);
    }

    /**Creating url of URL */
    private static URL createUrl(String Url){
            URL url = null;
            try{
                url = new URL((Url));
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG,"Problem Building Url");
            }
            return url;
    }

    /**making http request from {@link URL} and return jsonResponse, uses helper method readFromStream*/
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000); // kitne time tak wait karna
            urlConnection.setConnectTimeout(1500); //kitne time tak connection na mile toh terminate ho jaye
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error response code"+(String.valueOf(urlConnection.getResponseCode())));
            }
        }

        catch (IOException e){
            Log.e(LOG_TAG,"Problem recieving json file",e);
        }

        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**Convert the {@link InputStream} into a string which contains whole json response
     * */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    /**
     * Return a list of {@link EarthQuake} objects that has been built up from
     * parsing a JSON response.
     */
   private static List<EarthQuake> extractFeatureFromJson(String earthQuakeJson) {
        // Checking if json response nul
        if (TextUtils.isEmpty(earthQuakeJson)){
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthQuake> earthquakes = new ArrayList<>();

        //  If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

//            create Json from json response stream
            JSONObject root = new JSONObject(earthQuakeJson);
            JSONArray featuresArray = root.getJSONArray("features");

            JSONObject jsonProperties;
            for (int i = 0; i < featuresArray.length(); i++) {

                jsonProperties = featuresArray.getJSONObject(i).getJSONObject("properties");
                earthquakes.add(new EarthQuake((float) jsonProperties.getDouble("mag"),
                        jsonProperties.getString("place"),
                        jsonProperties.getLong("time"),
                        jsonProperties.getString("url")));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}