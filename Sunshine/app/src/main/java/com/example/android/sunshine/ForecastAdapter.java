package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {


    private final Context mContext;

    private final onClickHandler mClickListener;

    public interface onClickHandler {
        void onClick(String weatherForDay);
    }

    private Cursor mCursor;


    public ForecastAdapter(@NonNull Context context, onClickHandler clickHandler) {
        mContext = context;
        mClickListener = clickHandler;
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);

        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_ID);
//        HUMAN READABLE DATE
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);

        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String highAndLowTemp =
                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);

        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemp;

        holder.weatherSummary.setText(weatherSummary);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;

        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
//        Updating newCursor to notify change
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.forecast_list_item, parent, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);

    }


    //    For cache files of children view
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummary;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {

            String weatherForDay = weatherSummary.getText().toString();
            mClickListener.onClick(weatherForDay);
        }
    }


}
