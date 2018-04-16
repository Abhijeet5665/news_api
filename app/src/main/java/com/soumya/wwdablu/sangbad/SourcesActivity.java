package com.soumya.wwdablu.sangbad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.soumya.wwdablu.sangbad.fragments.IAction;
import com.soumya.wwdablu.sangbad.fragments.NewsSourcesFragment;
import com.soumya.wwdablu.sangbad.network.model.Sources;

public class SourcesActivity extends AppCompatActivity {

    private static final String FTAG_SOURCES = "sourcesFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);

        NewsSourcesFragment fragment = (NewsSourcesFragment) getSupportFragmentManager()
                .findFragmentByTag(FTAG_SOURCES);

        if(null == fragment) {

            fragment = new NewsSourcesFragment();
            getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_sources_activity, fragment, FTAG_SOURCES)
                .commit();
        }

        fragment.setActionListener(actionListener);
    }

    private IAction actionListener = new IAction() {
        @Override
        public void onSourceClicked(Sources source) {

            Intent articleListActivity = new Intent(SourcesActivity.this, ArticleActivity.class);
            articleListActivity.putExtra("sourceId", source.getId());
            articleListActivity.putExtra("sourceName", source.getName());
            startActivity(articleListActivity);
        }
    };
}
