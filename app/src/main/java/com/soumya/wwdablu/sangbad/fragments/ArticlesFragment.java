package com.soumya.wwdablu.sangbad.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.soumya.wwdablu.sangbad.R;
import com.soumya.wwdablu.sangbad.SettingsActivity;
import com.soumya.wwdablu.sangbad.adapters.ArticlesAdapter;
import com.soumya.wwdablu.sangbad.databinding.FragmentArticleListBinding;
import com.soumya.wwdablu.sangbad.dataprovider.ArticlesProvider;
import com.soumya.wwdablu.sangbad.network.model.Articles;

import java.util.LinkedList;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class ArticlesFragment extends BaseFragment {

    public static final String KEY_SOURCE_ID = "sourceId";

    private FragmentArticleListBinding binder;
    private ArticlesAdapter adapter;
    private DisposableObserver disposableObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_article_list, container, false);

        adapter = new ArticlesAdapter();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        binder.rvArticleList.setLayoutManager(layoutManager);
        binder.rvArticleList.setAdapter(adapter);

        setHasOptionsMenu(true);

        return binder.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(null != getArguments()) {
            String sourceId = getArguments().getString(KEY_SOURCE_ID, "");
            getArticleList(sourceId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int mode = Integer.parseInt(sp.getString(SettingsFragment.KEY_ARTICLE_MODE_LIST, "1"));
        adapter.setLayoutViewType(mode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != disposableObserver && !disposableObserver.isDisposed()) {
            disposableObserver.dispose();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.articles, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.menu_article_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void getArticleList(String sourceId) {

        //Guard check
        if(TextUtils.isEmpty(sourceId)) {
            return;
        }

        PublishSubject<LinkedList<Articles>> articlesList = PublishSubject.create();

        disposableObserver = articlesList
            .subscribeWith(new DisposableObserver<LinkedList<Articles>>() {
                @Override
                public void onNext(LinkedList<Articles> articles) {
                    adapter.setArticlesList(articles);
                    binder.pbLoadingArticles.setVisibility(View.GONE);
                    binder.rvArticleList.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    if(!disposableObserver.isDisposed()) {
                        disposableObserver.dispose();
                    }
                }
            });

        ArticlesProvider.getInstance().setObserver(articlesList);
        ArticlesProvider.getInstance().getSourceList(sourceId);
    }

    private int getSpanCount() {

        int rotation = getOrientation();
        int spanCount = 1;

        switch(rotation) {

            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                spanCount = 1;
                break;

            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                spanCount = 2;
                break;
        }

        return spanCount;
    }
}
