package com.example.android.sunshine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.android.sunshine.data.WeatherContract.WeatherEntry;
import com.example.android.sunshine.databinding.ActivityDetailsBinding;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";

    private static final String[] WeatherDetailProjections = {
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_HUMIDITY = 3;
    public static final int INDEX_WEATHER_PRESSURE = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_DEGREES = 6;
    public static final int INDEX_WEATHER_WEATHER_ID = 7;

    private static final int ID_DETAIL_LOADER = 353;

    private String mForecastSummary;
    private Uri mUri;


    private ActivityDetailsBinding mDetailsBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mDetailsBinding = DataBindingUtil.setContentView(DetailsActivity.this,R.layout.activity_details);
        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("Uri for details activity cannot be null");

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_share) {
            startActivity(createShareForecastIntent());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG).getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId){
            case ID_DETAIL_LOADER:{
                return new CursorLoader(this,
                        mUri,WeatherDetailProjections,
                        null,null,null);
            }
            default:
                throw new RuntimeException("Loader not implemented: " + loaderId);
        }


    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()){
            cursorHasValidData = true;
        }

        if(!cursorHasValidData) return;//        No data available

        int weatherId = data.getInt(INDEX_WEATHER_WEATHER_ID);
        int weatherIconID = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
        mDetailsBinding.primaryInfo.weatherIcon.setImageResource(weatherIconID);

        long localDateMidnightGmt = data.getLong(INDEX_WEATHER_DATE);
        String dateText = SunshineDateUtils.getFriendlyDateString(this,
                localDateMidnightGmt,true);
        mDetailsBinding.primaryInfo.date.setText(dateText);

        String description = SunshineWeatherUtils.getStringForWeatherCondition(this,weatherId);
        String descriptionAlly = getString(R.string.a11y_forecast,description);
        mDetailsBinding.primaryInfo.weatherDescription.setText(descriptionAlly);
        mDetailsBinding.primaryInfo.weatherDescription.setContentDescription(descriptionAlly);

        mDetailsBinding.primaryInfo.weatherIcon.setContentDescription(descriptionAlly);

        double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
        String highString = SunshineWeatherUtils.formatTemperature(this,highInCelsius);
        String highAlly  = getString(R.string.a11y_high_temp,highString);
        mDetailsBinding.primaryInfo.highTemp.setText(highString);
        mDetailsBinding.primaryInfo.highTemp.setContentDescription(highAlly);

        double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);
        String lowString = SunshineWeatherUtils.formatTemperature(this,lowInCelsius);
        String lowAlly = getString(R.string.a11y_low_temp,lowString);
        mDetailsBinding.primaryInfo.lowTemp.setText(lowString);
        mDetailsBinding.primaryInfo.lowTemp.setContentDescription(lowAlly);

        float humidity = data.getFloat(INDEX_WEATHER_HUMIDITY);
        String humidityString = getString(R.string.format_humidity,humidity);
        String humidityAlly = getString(R.string.a11y_humidity,humidityString);
        mDetailsBinding.extraDetails.humidity.setText(humidityString);
        mDetailsBinding.extraDetails.humidity.setContentDescription(humidityAlly);
        mDetailsBinding.extraDetails.humidityLabel.setContentDescription(humidityAlly);


        float windSpeed =  data.getFloat(INDEX_WEATHER_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_WEATHER_DEGREES);
        String windString = SunshineWeatherUtils.getFormattedWind(this,windSpeed,windDirection);
        String windAlly = getString(R.string.a11y_wind,windString);
        mDetailsBinding.extraDetails.windMeasurement.setText(windString);
        mDetailsBinding.extraDetails.windMeasurement.setContentDescription(windAlly);
        mDetailsBinding.extraDetails.windLabel.setContentDescription(windAlly);


        float pressure = data.getFloat(INDEX_WEATHER_PRESSURE);
        String pressureString = getString(R.string.format_pressure,pressure);
        String pressureAlly = getString(R.string.a11y_pressure,pressureString);
        mDetailsBinding.extraDetails.pressure.setText(pressureString);
        mDetailsBinding.extraDetails.pressure.setContentDescription(pressureAlly);
        mDetailsBinding.extraDetails.pressureLabel.setContentDescription(pressureAlly);

        mForecastSummary = String.format("%s - %s - %s%s",
                dateText,description,highString,lowString);

    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {

    }
}


