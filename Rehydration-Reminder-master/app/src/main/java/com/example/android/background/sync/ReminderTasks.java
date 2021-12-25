package com.example.android.background.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.android.background.utilities.PreferenceUtilities;

public class ReminderTasks{

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public static void executeTask(Context context,String action){
        if (action == ACTION_INCREMENT_WATER_COUNT)
        {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context) {

        PreferenceUtilities.incrementWaterCount(context);

    }



}

