package com.example.android.todolist;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;


public class AddTaskActivity extends AppCompatActivity {


    private int mPriority;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;


    }


    public void onClickAddTask(View view) {


    }

    public void onPrioritySelected(View view) {

        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }

    }


}
