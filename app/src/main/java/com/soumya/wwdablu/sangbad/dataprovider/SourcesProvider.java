package com.soumya.wwdablu.sangbad.dataprovider;

import com.soumya.wwdablu.sangbad.network.RetrofitProvider;
import com.soumya.wwdablu.sangbad.network.model.SourceResponse;
import com.soumya.wwdablu.sangbad.network.model.Sources;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class SourcesProvider {

    private static SourcesProvider instance;
    private SourceResponse sourceResponse;

    private SourcesProvider() {
        //
    }

    public static synchronized SourcesProvider getInstance() {

        if(null == instance) {
            instance = new SourcesProvider();
        }

        return instance;
    }

    public Observable<LinkedList<Sources>> getSourceList() {

        return Observable.defer(new Callable<ObservableSource<SourceResponse>>() {
            @Override
            public ObservableSource<SourceResponse> call() throws Exception {

                if(null == sourceResponse) {
                    return RetrofitProvider.getServiceAPI().getSourceList();
                }

                return Observable.just(sourceResponse);
            }
        })

        .map(new Function<SourceResponse, LinkedList<Sources>>() {
            @Override
            public LinkedList<Sources> apply(SourceResponse sourceResponse) throws Exception {
                SourcesProvider.this.sourceResponse = sourceResponse;
                return sourceResponse.getSources();
            }
        });
    }
}