package com.soumya.wwdablu.sangbad.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.soumya.wwdablu.sangbad.R;
import com.soumya.wwdablu.sangbad.SettingsActivity;
import com.soumya.wwdablu.sangbad.adapters.ISourceAction;
import com.soumya.wwdablu.sangbad.adapters.NewsSourcesAdapter;
import com.soumya.wwdablu.sangbad.databinding.FragmentSourceListBinding;
import com.soumya.wwdablu.sangbad.dataprovider.SourcesProvider;
import com.soumya.wwdablu.sangbad.network.model.Sources;

import java.util.LinkedList;

import icepick.Icepick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsSourcesFragment extends BaseFragment {

    private FragmentSourceListBinding binder;
    private NewsSourcesAdapter adapter;
    private DisposableObserver disposableObserver;
    private IAction actionListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_source_list, container, false);
        adapter = new NewsSourcesAdapter(sourceAction);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);

        binder.rvSourceList.setLayoutManager(layoutManager);
        binder.rvSourceList.setAdapter(adapter);

        //Enable the option menu
        setHasOptionsMenu(true);

        return binder.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSourcesList();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int mode = Integer.parseInt(sp.getString(SettingsFragment.KEY_SOURCE_MODE_LIST, "1"));
        adapter.setLayoutViewType(mode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
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
        inflater.inflate(R.menu.sources, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_source_search).getActionView();
        handleSearchView(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.menu_source_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void setActionListener(IAction actionListener) {
        this.actionListener = actionListener;
    }

    private void getSourcesList() {

        disposableObserver = SourcesProvider.getInstance().getSourceList()

        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

        .subscribeWith(new DisposableObserver<LinkedList<Sources>>() {
            @Override
            public void onNext(LinkedList<Sources> sourceResponse) {
                adapter.setSourcesList(sourceResponse);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "Error is network call");
            }

            @Override
            public void onComplete() {
                if(!disposableObserver.isDisposed()) {
                    disposableObserver.dispose();
                }
            }
        });
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

    private ISourceAction sourceAction = new ISourceAction() {
        @Override
        public void onSourceClick(Sources source, int position) {

            if(null != actionListener) {
                actionListener.onSourceClicked(source);
            }
        }
    };

    private void handleSearchView(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
