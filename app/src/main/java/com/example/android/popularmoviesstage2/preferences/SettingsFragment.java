package com.example.android.popularmoviesstage2.preferences;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.android.popularmoviesstage2.R;

/**
 * Created by Suraz Verma on 12/30/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.movie_pref);
    }
}
