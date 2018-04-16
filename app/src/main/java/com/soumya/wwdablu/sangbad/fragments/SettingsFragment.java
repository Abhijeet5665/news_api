package com.soumya.wwdablu.sangbad.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.soumya.wwdablu.sangbad.R;

public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_SOURCE_MODE_LIST = "key_list_source_mode";
    public static final String KEY_ARTICLE_MODE_LIST = "key_list_article_mode";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
