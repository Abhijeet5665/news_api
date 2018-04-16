package com.soumya.wwdablu.sangbad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.soumya.wwdablu.sangbad.fragments.ArticlesFragment;

import icepick.Icepick;
import icepick.State;

public class ArticleActivity extends AppCompatActivity {

    private static final String FTAG_ARTICLES = "articlesFragment";

    @State
    String articleSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Icepick.restoreInstanceState(this, savedInstanceState);

        ArticlesFragment fragment = (ArticlesFragment) getSupportFragmentManager()
                .findFragmentByTag(FTAG_ARTICLES);


        if(null != getIntent()) {
            articleSource = getIntent().getStringExtra("sourceName");
        }

        //Set the actionbar title
        getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " - " + articleSource);

        if(null == fragment) {

            Bundle bundle = new Bundle(2);
            bundle.putString(ArticlesFragment.KEY_SOURCE_ID, getIntent().getStringExtra("sourceId"));

            fragment = new ArticlesFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_articles_activity, fragment, FTAG_ARTICLES)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
