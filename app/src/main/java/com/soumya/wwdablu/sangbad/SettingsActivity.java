package com.soumya.wwdablu.sangbad;

import android.os.Bundle;

import com.soumya.wwdablu.sangbad.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String FTAG_SETTINGS = "settingsFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsFragment fragment = (SettingsFragment) getFragmentManager()
                .findFragmentByTag(FTAG_SETTINGS);

        if(null == fragment) {

            fragment = new SettingsFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, fragment, FTAG_SETTINGS)
                    .commit();
        }
    }
}
