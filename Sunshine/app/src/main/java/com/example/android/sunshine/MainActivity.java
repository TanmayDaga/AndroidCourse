package com.example.android.sunshine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView errorMessageTextView;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        errorMessageTextView = (TextView) findViewById(R.id.error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);
        loadWeatherData();

    }

    private void loadWeatherData(){
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new fetchWeatherTask().execute(location);
    }

    private void showWeatherDataView(){
        errorMessageTextView.setVisibility(View.INVISIBLE);
        mWeatherTextView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        errorMessageTextView.setVisibility(View.VISIBLE);
        mWeatherTextView.setVisibility(View.INVISIBLE);
    }

    public class fetchWeatherTask extends AsyncTask<String, Void, String[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            /*if zip code empty or not*/
            if (params.length == 0){
                return null;
            }
            else{
                String location = params[0];
                URL weatherRequestUrl = NetworkUtils.buildUrl(location);
                try {
                    String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils.
                            getSimpleWeatherStringsFromJson(MainActivity.this,jsonWeatherResponse);
                    return simpleJsonWeatherData;
                }
                catch (Exception e){
                    e.printStackTrace();
                    return null;
                }


            }

        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null){
                showWeatherDataView();
                for (String weatherString: weatherData) {
                    mWeatherTextView.append((weatherString)+"\n\n");
                }

            }
            else {
                showErrorMessage();
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh){
            mWeatherTextView.setText("");
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}