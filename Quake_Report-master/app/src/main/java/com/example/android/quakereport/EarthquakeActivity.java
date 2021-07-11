/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {



    /**
     * Url for earthquake data
     */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=5&limit=12";
    private EarthQuakeAdapter earthQuakeAdapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final int EARTHQUAKE_LOADER_ID = 1;

    TextView emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

// Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthQuakeAdapter = new EarthQuakeAdapter(this, new ArrayList<EarthQuake>());

        emptyView =  (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(emptyView);




        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthQuake earthQuakeObject = earthQuakeAdapter.getItem(i);
//               Convert String url to URI
                Intent websiteView = new Intent(Intent.ACTION_VIEW, Uri.parse(earthQuakeObject.getUrl()));
                startActivity(websiteView);


            }
        });
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthQuakeAdapter);


        // Checking if net available or not
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo  networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
        else {
            ((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
            emptyView.setText("Network not found");
        }

    }


    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {
        // Create new loader for given data
        return new EarthQuakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes) {

        View loadingIncdicator = (ProgressBar) findViewById(R.id.progressBar);
        loadingIncdicator.setVisibility(View.GONE);
        // NO data till here so show
        emptyView.setText(R.string.no_eerthquakes);
//        Clear the adapter and add new adapter
        earthQuakeAdapter.clear();
        if (earthQuakes != null && !earthQuakes.isEmpty()){
            earthQuakeAdapter.addAll(earthQuakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
//        Loader reset so clear existing data
        earthQuakeAdapter.clear();
    }
}

