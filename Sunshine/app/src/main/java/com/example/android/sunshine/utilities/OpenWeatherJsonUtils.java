package com.example.android.sunshine.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class OpenWeatherJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     */

    /*LOCATION INFORMATION*/
    private static final String OWM_CITY = "city";
    private static final String OWM_COORD = "coord";

    /*Location Coordinates*/
    private static final String OWM_LATITUDE = "lat";
    private static final String OWM_LONGITUDE = "lon";

    /* Weather information. Each day's forecast info is an element of the "list" array */
    private static final String OWM_LIST = "list";

    /* All temperatures are children of the "temp" object */
    private static final String OWM_TEMPERATURE = "temp";

    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";
    /* Max temperature for the day */
    private static final String OWM_MAX = "max";
    private static final String OWM_MIN = "min";

    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_ID = "id";
    private static final String OWM_MESSAGE_CODE = "cod";

    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {



        /* String array to hold each day's weather String */
        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        parsedWeatherData = new String[weatherArray.length()];


        long startDay = SunshineDateUtils.getNormalizedUtcDateForToday();

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;

            /* These are the values that will be collected */
            long dateTimeMillis;
            double high;
            double low;
            int weatherId;
            String description;

            /* Get the JSON object representing the day */
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false);

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            weatherId = weatherObject.getInt(OWM_WEATHER_ID);
            description = SunshineWeatherUtils.getStringForWeatherCondition(context, weatherId);

            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);
            low = temperatureObject.getDouble(OWM_MIN);
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getWeatherContentValuesFromJson(Context context, String forecastJsonStr) throws JSONException {
        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST);
        JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
        JSONObject citycoord = cityJson.getJSONObject(OWM_COORD);
        double cityLatittude = citycoord.getDouble(OWM_LATITUDE);
        double cityLongitude = citycoord.getDouble(OWM_LONGITUDE);

        SunshinePreferences.setLocationDetails(context, cityLatittude, cityLongitude);
        ContentValues[] weatherContentVAlues = new ContentValues[jsonWeatherArray.length()];
        long normaliseUtcStartDay = SunshineDateUtils.getNormalizedUtcDateForToday();
        for (int i = 0; i < jsonWeatherArray.length(); i++) {
            long dateTimeMillis;
            double pressure;
            int humidity;
            double windSpeed;
            double windDirection;

            double high;
            double low;

            int weatherId;

            JSONObject dayForeCast = jsonWeatherArray.getJSONObject(i);
            dateTimeMillis = normaliseUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            pressure = dayForeCast.getDouble(OWM_PRESSURE);
            humidity = dayForeCast.getInt(OWM_HUMIDITY);
            windSpeed = dayForeCast.getDouble(OWM_WINDSPEED);
            windDirection = dayForeCast.getDouble(OWM_WIND_DIRECTION);

            JSONObject weatherObject = dayForeCast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            weatherId = weatherObject.getInt(OWM_WEATHER_ID);

            JSONObject temperatureObject = dayForeCast.getJSONObject(OWM_TEMPERATURE);
            high = temperatureObject.getDouble(OWM_MAX);
            low = temperatureObject.getDouble(OWM_MIN);

            ContentValues weatherValues = new ContentValues();
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,weatherId);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeMillis);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);

            weatherContentVAlues[i] = weatherValues;
        }

        return weatherContentVAlues;

    }
}