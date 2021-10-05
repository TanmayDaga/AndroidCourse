package com.example.explicitintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mNameEntry;
    private Button mDoSomethingCool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDoSomethingCool = (Button) findViewById(R.id.buttonDoSomethingCool);
        mNameEntry = (EditText) findViewById(R.id.eptextentry);

        mDoSomethingCool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = mNameEntry.getText().toString();
                Context context = MainActivity.this;
                Class destinationActivity = ChildActivity.class;
                Intent startChildActivityIntent = new Intent(context,destinationActivity);

                startChildActivityIntent.putExtra(Intent.EXTRA_TEXT,text);
                startActivity(startChildActivityIntent);
            }
        });
    }
}