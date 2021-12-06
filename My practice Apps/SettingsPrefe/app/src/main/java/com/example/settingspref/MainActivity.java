package com.example.settingspref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button clickTogoButton;
    SharedPreferences sharedPreferences;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       textView = (TextView) findViewById(R.id.textView);

        clickTogoButton = (Button) findViewById(R.id.buttonToGo);
        clickTogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SettingsAcitvity.class));
            }
        });

        setValues();


    }
    private void setValues(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String checkboxVal = sharedPreferences.getBoolean(getString(R.string.checkboxKey),true)?"True":"False";
        String keySelecetedListPREF = sharedPreferences.getString(getString(R.string.listPrefKey),getString(R.string.l1_key));

        textView.setText("Edit Text:"+sharedPreferences.getString(getString(R.string.edithKey),"")+"\n"+
                "CheckBox:"+checkboxVal+"\nListPRefernce:");
        if (keySelecetedListPREF.equals(getString(R.string.l1_key))){
            textView.append(getString(R.string.l1_option));
        }
        else {
            textView.append(getString(R.string.l2_option));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        setValues();
    }
}