package com.soumya.wwdablu.sangbad.network;

import com.soumya.wwdablu.sangbad.network.model.ArticleResponse;
import com.soumya.wwdablu.sangbad.network.model.SourceResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceAPI {

    @GET("v1/sources")
    Observable<SourceResponse> getSourceList();

    @GET("v1/articles")
    Observable<ArticleResponse> getArticlesForSource(@Query("source") String source);
}
