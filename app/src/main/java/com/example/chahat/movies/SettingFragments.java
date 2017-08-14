package com.example.chahat.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * Created by chahat on 18/7/17.
 */

public class SettingFragments extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_setting);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for (int i=0;i<count;i++){
            Preference p = preferenceScreen.getPreference(i);

            if (!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(),getString(R.string.pref_option_sort_value_popular));
                setPreferenceSummary(p,value);
            }
        }
    }

    private void setPreferenceSummary(Preference p, String value){
        if (p instanceof ListPreference){

            ListPreference listPreference = (ListPreference) p;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex>=0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Preference p = findPreference(s);
        if (p!=null){
            if (!(p instanceof CheckBoxPreference)){
                String value = p.getSharedPreferences().getString(p.getKey(),getString(R.string.pref_option_sort_value_popular));
                setPreferenceSummary(p,value);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
