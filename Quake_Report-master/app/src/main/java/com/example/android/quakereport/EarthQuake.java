package com.example.android.quakereport;

import java.text.DecimalFormat;

public class EarthQuake {
    private Float magnitude;
    private String location;
    private String date;
    private Long timeInMilliSeconds;
    private String url;

    public EarthQuake(Float magnitude, String location, long timeInMilliSeconds,String url) {
        this.magnitude = Float.valueOf(new DecimalFormat("0.0").format(magnitude));
        this.location = location;
        this.timeInMilliSeconds = timeInMilliSeconds;
        this.url = url;
    }

    public Float getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeInMilliSeconds() {
        return timeInMilliSeconds;
    }

    public String getUrl() { return url; }
}
