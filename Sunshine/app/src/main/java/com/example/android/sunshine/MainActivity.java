package com.example.android.sunshine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.FakeDataUtils;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;


import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.onClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private ProgressBar mLoadingIndicator;

    private static final String TAG = MainActivity.class.getSimpleName();

    //Column Name of projection
    public static final String[] MAIN_FORECAST_PROJECTION = {

            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID

    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_ID = 3;

    private static final int ID_FORECAST_LOADER = 44;

    private int mPostion = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        FakeDataUtils.insertFakeData(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);//Improve performance
        mForecastAdapter = new ForecastAdapter(this, this);
        mRecyclerView.setAdapter(mForecastAdapter);


        showLoading();
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);

    }


    private void openPreferedLocationInMap() {

        double[] locationCoordinates = SunshinePreferences.getLocationCoordinates(this);
        String posLat = Double.toString(locationCoordinates[0]);
        String posLong = Double.toString(locationCoordinates[1]);
        Uri geoLocation = Uri.parse("geo: " + posLat + " , " + posLong);
        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call" + geoLocation.toString() + ", no recieving app are installed");
        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle bundle) {

        switch (loaderId) {
            case ID_FORECAST_LOADER:
                Uri foreCastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC ";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this, foreCastQueryUri, MAIN_FORECAST_PROJECTION,
                        selection, null, sortOrder);
            default:
                throw new RuntimeException("Loader not implemented" + loaderId);
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mForecastAdapter.swapCursor(data);

        if (mPostion == RecyclerView.NO_POSITION) mPostion = 0;

        mRecyclerView.scrollToPosition(mPostion);
        if (data.getCount() != 0) showWeatherDataView();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }


    public void onClick(long date) {


        Intent intentToStartDetailsActivity = new Intent(MainActivity.this, DetailsActivity.class);

        Uri uriForDateClick = WeatherContract.WeatherEntry.builtWeatherUriWithDate(date);
        intentToStartDetailsActivity.setData(uriForDateClick);

        startActivity(intentToStartDetailsActivity);


    }

    private void showWeatherDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_map) {
            openPreferedLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}