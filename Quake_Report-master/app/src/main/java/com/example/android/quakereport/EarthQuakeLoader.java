package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<EarthQuake>> {


    private String url;
    public EarthQuakeLoader(Context context, String url) {

        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        if (TextUtils.isEmpty(url)){
            return null;
        }
        List<EarthQuake> earthQuakes = QueryUtils.fetchEarthQuakeData(this.url);
        return earthQuakes;
    }
}
