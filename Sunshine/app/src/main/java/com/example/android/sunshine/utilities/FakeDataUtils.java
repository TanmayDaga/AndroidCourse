package com.example.android.sunshine.utilities;

import android.content.ContentValues;

import com.example.android.sunshine.data.WeatherContract.WeatherEntry;

public class FakeDataUtils {

    private static int[] weather_ID = {200, 300, 500, 711, 900, 962};

    private static ContentValues createTestWeatherContentValues(long date){
        ContentValues testWeatherValues = new ContentValues();

        testWeatherValues.put(WeatherEntry.COLUMN_DATE,date);
        testWeatherValues.put(WeatherEntry.COLUMN_DEGREES,Math.random()*2);
        testWeatherValues.put(WeatherEntry.COLUMN_HUMIDITY,Math.random()*100);
        testWeatherValues.put(WeatherEntry.COLUMN_PRESSURE,875+Math.random()*100);

        int MaxTemp = (int) (Math.random()*100);
        testWeatherValues.put(WeatherEntry.COLUMN_MAX_TEMP,MaxTemp);
        testWeatherValues.put(WeatherEntry.COLUMN_MIN_TEMP,MaxTemp - ((int) (Math.random()*100)));

    }






}