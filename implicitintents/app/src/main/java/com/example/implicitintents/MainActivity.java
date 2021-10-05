package com.example.implicitintents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickOpenWebPageButton(View view){
        Uri url = Uri.parse("https://www.google.com/?client=safari");
        Intent intent = new Intent(Intent.ACTION_VIEW,url);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }

    }


    public void onClickOpenAddressButton(View view){
        String address = "1688 Amphithreater Parkway, CA";
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("geo").path("0,0").appendQueryParameter("q",address);
        Intent intent = new Intent(Intent.ACTION_VIEW,builder.build());
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }



    public void onClickShareTextButton(View view){

    }
    public void myOwnButton(View view){

    }
}