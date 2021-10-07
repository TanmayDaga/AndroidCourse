package com.example.lifecycle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextView;

    private static final String LIFE_CYCLE_CALLBACKS = "callbacks";
    private static final ArrayList<String> mLifeCycleCallbacks = new ArrayList<>();

    //    Creating constants
    private static final String ON_CREATE = "onCreate";
    private static final String ON_START = "onStart";
    private static final String ON_RESUME = "onResume";
    private static final String ON_PAUSE = "onPause";
    private static final String ON_STOP = "onStop";
    private static final String ON_RESTART = "onRestart";
    private static final String ON_DESTROY = "onDestroy";
    private static final String ON_SAVE_INSTANCE_STATE = "onSaveInstanceState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_lifecyle_events_display);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFE_CYCLE_CALLBACKS)) {
                String oldState = savedInstanceState.getString(LIFE_CYCLE_CALLBACKS);
                mTextView.setText(oldState);
            }
        }
        for (int i = mLifeCycleCallbacks.size() -1;i>=0;i--){
            mTextView.append(mLifeCycleCallbacks.get(i)+"\n");

        }
        mLifeCycleCallbacks.clear();
        LogAndAppend(ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogAndAppend(ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogAndAppend(ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogAndAppend(ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mLifeCycleCallbacks.add(0,ON_STOP);
        LogAndAppend(ON_STOP);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogAndAppend(ON_RESTART);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifeCycleCallbacks.add(0,ON_DESTROY);
        LogAndAppend(ON_DESTROY);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LogAndAppend(ON_SAVE_INSTANCE_STATE);
        String lifecycleDisplayViewContents = mTextView.getText().toString();
        outState.putString(LIFE_CYCLE_CALLBACKS, lifecycleDisplayViewContents);
    }

    public void resetLifeCycle(View view) {
        mTextView.setText("LifeCycle Callbacks" + "\n");
    }

    private void LogAndAppend(String s) {
        Log.d(TAG, "Life cycle event " + s);
        mTextView.append(s + "\n");
    }


}