package com.example.photoframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//TODO itemclicklistner
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoAdapterViewHolder>{
    Bitmap[] data;
    @NonNull
    @Override
    public PhotoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new PhotoAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapterViewHolder holder, int position) {
        Bitmap image = data[position];
        holder.imageView.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return 12;//Only for trial
    }

    public class PhotoAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public PhotoAdapterViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);


        }

    }
}
