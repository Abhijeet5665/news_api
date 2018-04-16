package com.soumya.wwdablu.sangbad.dataprovider;

import com.soumya.wwdablu.sangbad.network.RetrofitProvider;
import com.soumya.wwdablu.sangbad.network.model.ArticleResponse;
import com.soumya.wwdablu.sangbad.network.model.Articles;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class ArticlesProvider {

    private static ArticlesProvider instance;

    private LinkedList<Articles> articlesList;
    private PublishSubject<LinkedList<Articles>> listPublishSubject;
    private DisposableObserver disposableObserver;
    private String lastSourceId;

    private ArticlesProvider() {
        //
    }

    public static synchronized ArticlesProvider getInstance() {

        if(null == instance) {
            instance = new ArticlesProvider();
        }

        return instance;
    }

    public void setObserver(PublishSubject<LinkedList<Articles>> listPublishSubject) {
        this.listPublishSubject = listPublishSubject;
    }

    public void getSourceList(final String sourceId) {

        //Guard check
        if(null != articlesList && lastSourceId.contentEquals(sourceId)) {
            publishResult();
            return;
        }

        disposableObserver = Observable.defer(new Callable<ObservableSource<ArticleResponse>>() {
            @Override
            public ObservableSource<ArticleResponse> call() throws Exception {
                return RetrofitProvider.getServiceAPI().getArticlesForSource(sourceId);
            }
        })

        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

        .subscribeWith(new DisposableObserver<ArticleResponse>() {
            @Override
            public void onNext(ArticleResponse articleResponse) {
                articlesList = articleResponse.getArticles();
                lastSourceId = sourceId;
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

                publishResult();

                if(!disposableObserver.isDisposed()) {
                    disposableObserver.dispose();
                }
            }
        });
    }

    private void publishResult() {

        if(null == listPublishSubject || !listPublishSubject.hasObservers()) {
            return;
        }

        listPublishSubject.onNext(articlesList);
        listPublishSubject.onComplete();
    }
}
