package com.example.android.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

    private String[] mWeatherData;
    private final onClickHandler mClickListener;

    public interface onClickHandler{
        void onClick(String weatherForDay);
    }

    public ForecastAdapter(onClickHandler clickHandler){mClickListener = clickHandler;}


    //    For cache files of children view
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mWeatherTextView;

        public  ForecastAdapterViewHolder(View view){
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = mWeatherData[adapterPosition];
            mClickListener.onClick(weatherForDay);
        }
    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_list_item,parent,false);
        return new ForecastAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder( ForecastAdapterViewHolder holder, int position) {
        String mWeatherDataForThisDay = mWeatherData[position];
        holder.mWeatherTextView.setText(mWeatherDataForThisDay);
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) {
            return 0;
        }
        return mWeatherData.length;
    }

    public void setmWeatherData(String [] weatherData){
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
