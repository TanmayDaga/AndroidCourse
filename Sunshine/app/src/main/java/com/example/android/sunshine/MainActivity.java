package com.example.android.sunshine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView) findViewById(R.id.weather_data);
        String[] dummyDatas={
                "today, May 17 - Cleat - 17*C / 15*C",
                ""
        };
        for (String dummyData:

            dummyDatas ) {
            mWeatherTextView.append(dummyData);
        }
    }
}