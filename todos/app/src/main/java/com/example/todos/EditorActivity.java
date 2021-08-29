package com.example.todos;

import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditorActivity extends AppCompatActivity {

    CalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Setting minimum date of calendar view
        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setMinDate(Calendar.getInstance().getTimeInMillis());
    }
}