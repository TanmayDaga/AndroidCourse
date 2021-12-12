package com.example.android.sunshine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;


import java.net.URL;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.onClickHandler,
        LoaderManager.LoaderCallbacks<String[]>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView errorMessageTextView;
    private ProgressBar mLoadingIndicator;

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final int FORECAST_LOADER_ID = 0;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        errorMessageTextView = (TextView) findViewById(R.id.error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);//Improve performance
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);

        int loaderId = FORECAST_LOADER_ID;
        LoaderManager.LoaderCallbacks<String[]> callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, callbacks);

        Log.d(TAG, "onCreate : registering preference changed listener");
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {

            Log.d(TAG, "onStart : preferences were updated");
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this); // Restarting the activity
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }


    public void onClick(String weatherForDAy) {

        Context context = this;
        Class destinationActivity = DetailsActivity.class;

        Intent intentToStartDetailsActivity = new Intent(context, destinationActivity);

        intentToStartDetailsActivity.putExtra(Intent.EXTRA_TEXT, weatherForDAy);

        startActivity(intentToStartDetailsActivity);


    }

    private void showWeatherDataView() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        errorMessageTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mWeatherData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (mWeatherData == null) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                } else {
                    deliverResult(mWeatherData);
                }
            }

            @Override
            public String[] loadInBackground() {

                URL url = NetworkUtils.getUrl(MainActivity.this);

                try {
                    String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils.
                            getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
                    return simpleJsonWeatherData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                mWeatherData = data;
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                super.deliverResult(data);
            }
        };

    }


    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {
        mForecastAdapter.setmWeatherData(data);

        if (data != null) {
            showWeatherDataView();
        } else {
            showErrorMessage();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    private void invalidateData() {
        mForecastAdapter.setmWeatherData(null);
    }


    private void openLocationInMap() {
        String addressString = SunshinePreferences.getPreferredWeatherLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);
        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call" + geoLocation.toString() + ", no recieving app are installed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            invalidateData();
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;
        }
        if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        }
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}