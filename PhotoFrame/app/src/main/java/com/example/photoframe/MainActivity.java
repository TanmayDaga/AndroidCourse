package com.example.photoframe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,GridLayoutManager.DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mPhotoAdapter = new PhotoAdapter();
        mRecyclerView.setAdapter(mPhotoAdapter);

    }

    //TODO
    public class fetchPhotos extends AsyncTask<Void,Void, Bitmap[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap[] doInBackground(Void... voids) {
            return new Bitmap[0];
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            super.onPostExecute(bitmaps);
        }
    }
}