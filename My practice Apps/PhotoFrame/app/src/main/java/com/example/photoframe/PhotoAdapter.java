package com.example.photoframe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//TODO itemclicklistner
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoAdapterViewHolder>{
    Bitmap[] data;
    private Context context;
    public onClickHandler AdapterclickHandler;
    public interface onClickHandler{
        public void onClick(String text);
    }

    public PhotoAdapter(onClickHandler onClickHadler){AdapterclickHandler = onClickHadler;}
    @NonNull
    @Override
    public PhotoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        return new PhotoAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapterViewHolder holder, int position) {
        new fetchPhotos().execute(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return 12;//Only for trial
    }

    public class PhotoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView imageView;
        public PhotoAdapterViewHolder(@NonNull View itemView) {

            super(itemView);
            itemView.setOnClickListener(this::onClick);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);



        }

        @Override
        public void onClick(View view) {
            AdapterclickHandler.onClick(String.valueOf(getAdapterPosition()));
        }
    }
    public  void setImages(Bitmap[] images){
        data = images;
        notifyDataSetChanged();
    }

    public class fetchPhotos extends AsyncTask<ImageView, Void, Bitmap> {
        public ImageView[] imgview;
        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            imgview = imageViews;
            return getImage("https://source.unsplash.com/random");


        }

        private Bitmap getImage(String imageUrl)
        {
            Bitmap image = null;
            int inSampleSize = 0;


            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;

            options.inSampleSize = inSampleSize;

            try
            {
                URL url = new URL(imageUrl);

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                InputStream stream = connection.getInputStream();

                image = BitmapFactory.decodeStream(stream, null, options);



                    options.inJustDecodeBounds = false;

                    connection = (HttpURLConnection)url.openConnection();

                    stream = connection.getInputStream();

                    image = BitmapFactory.decodeStream(stream, null, options);

                    return image;

            }

            catch(Exception e)
            {
                Log.e("getImage", e.toString());
            }

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgview[0].setImageBitmap(bitmap);

        }
    }}