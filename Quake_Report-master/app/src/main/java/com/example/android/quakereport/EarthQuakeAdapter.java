package com.example.android.quakereport;

import  android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {
    private Date dateObject;
    private String place;
    private final String LOCATION_OFSETTER = " of ";

    public EarthQuakeAdapter(Context context, ArrayList<EarthQuake> earthQuakes) {
        super(context, 0, earthQuakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        EarthQuake earthQuake = getItem(position);

        // Setting magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeView.setText(String.valueOf(earthQuake.getMagnitude()));
        // Set proper background color on circle
        ((GradientDrawable) magnitudeView.getBackground()).setColor(ContextCompat.getColor(getContext(), getMagnitudeColor(earthQuake.getMagnitude())));


        // Setting place
        /*
        Setting for location offset and location
        */
        place = earthQuake.getLocation();
        if (place.contains(LOCATION_OFSETTER)) {
            String[] parts = place.split(LOCATION_OFSETTER);
            ((TextView) listItemView.findViewById(R.id.locationOffset)).setText((parts[0] + LOCATION_OFSETTER));
            ((TextView) listItemView.findViewById(R.id.location)).setText(parts[1]);
        } else {
            ((TextView) listItemView.findViewById(R.id.locationOffset)).setText(getContext().getString(R.string.near_the));
            ((TextView) listItemView.findViewById(R.id.location)).setText(place);
        }


        /*
        Setting date and time
        */
        dateObject = new Date(earthQuake.getTimeInMilliSeconds());
        ((TextView) listItemView.findViewById(R.id.date)).setText(formatDate(dateObject));
        ((TextView) listItemView.findViewById(R.id.time)).setText(formatTime(dateObject));
        return listItemView;
    }

    private String formatDate(Date dateobject) {
        return new SimpleDateFormat("LLL dd,yyyy").format(dateobject);

    }

    private String formatTime(Date date) {
        return new SimpleDateFormat("h:mm a").format(date);
    }

    private String[] getPlaceSplit(String str) {
        return str.split(" of ");
    }

    private int getMagnitudeColor(Float magnitude) {
        int mag = (int) Math.floor(magnitude);
        switch (mag) {
            case 0:
            case 1:
                return R.color.magnitude1;
            case 2:
                return R.color.magnitude2;
            case 3:
                return R.color.magnitude3;
            case 4:
                return R.color.magnitude4;
            case 5:
                return R.color.magnitude5;
            case 6:
                return R.color.magnitude6;
            case 7:
                return R.color.magnitude7;
            case 8:
                return R.color.magnitude8;
            case 9:
                return R.color.magnitude9;
            case 10:
            default:
                return R.color.magnitude10plus;
        }

    }
}
