package com.example.settingspref;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

public class SettingsFragement extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_set);
        PreferenceScreen prefScreen = getPreferenceScreen();

        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (p instanceof ListPreference){
                ListPreference listPreference = (ListPreference) p;
                setListPrefsummary(listPreference,listPreference.getValue());
            }
            else if (p instanceof EditTextPreference){
                EditTextPreference editTextPreference = (EditTextPreference) p;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }

    }
    private void setListPrefsummary(ListPreference listPreference,String  key){
        listPreference.setSummary(listPreference.getEntries()[listPreference.findIndexOfValue(key)]);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (p instanceof ListPreference){
                ListPreference listPreference = (ListPreference) p;
                setListPrefsummary(listPreference,listPreference.getValue());
            }
            else if (p instanceof EditTextPreference){
                EditTextPreference editTextPreference = (EditTextPreference) p;
                editTextPreference.setSummary(editTextPreference.getText());
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }
}
