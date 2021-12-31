package com.example.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {


    private final Context mContext;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private final onClickHandler mClickListener;

    public interface onClickHandler {
        void onClick(long date);
    }

    private boolean mUseTodayLayout;

    private Cursor mCursor;


    public ForecastAdapter(@NonNull Context context, onClickHandler clickHandler) {
        mContext = context;
        mClickListener = clickHandler;
        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        int weatherImageId;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherId);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type" + viewType);
        }

        holder.iconView.setImageResource(weatherImageId);


        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        holder.dateView.setText(dateString);


//        HUMAN READABLE DATE
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        String descriptionAlly = mContext.getString(R.string.a11y_forecast, description);
        holder.descriptionView.setText(descriptionAlly);

        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
        String highAlly = mContext.getString(R.string.a11y_high_temp, highString);
        holder.hightTempView.setText(highString);
        holder.hightTempView.setContentDescription(highAlly);

        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        String lowAlly = mContext.getString(R.string.a11y_low_temp, lowString);
        holder.lowTempView.setText(lowString);
        holder.lowTempView.setContentDescription(lowAlly);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;

        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && (position == 0)) {
            return VIEW_TYPE_TODAY;

        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    void swapCursor(Cursor newCursor) {
//        Updating newCursor to notify change
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        int layoutId;
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecat_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.forecast_list_item;
                break;
            default:
                throw new IllegalArgumentException("Invalid view type" + viewType);
        }
        View view = LayoutInflater
                .from(mContext)
                .inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new ForecastAdapterViewHolder(view);

    }


    //    For cache files of children view
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView dateView;
        final TextView descriptionView;
        final TextView hightTempView;
        final TextView lowTempView;
        final ImageView iconView;

        public ForecastAdapterViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            hightTempView = (TextView) view.findViewById(R.id.high_temp);
            lowTempView = (TextView) view.findViewById(R.id.low_temp);
            view.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickListener.onClick(dateInMillis);
        }
    }


}
