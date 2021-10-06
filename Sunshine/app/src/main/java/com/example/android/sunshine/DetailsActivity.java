package com.example.android.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private String mForecast;

    private TextView mWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mWeatherData = (TextView) findViewById(R.id.tv_display_details);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                mForecast = intent.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherData.setText(mForecast);
            }
        }
    }
    private Intent createSahreForecastIntent(){
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast+FORECAST_SHARE_HASHTAG).getIntent();
        return shareIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createSahreForecastIntent());
        return true;
    }
}

