package com.soumya.wwdablu.sangbad.network;

import com.soumya.wwdablu.sangbad.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    private static final String BASE_URL = "https://newsapi.org/";
    private static ServiceAPI serviceAPI;

    private RetrofitProvider() {

        //Create the interceptor
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request originalRequest = chain.request();
                HttpUrl originalHttpUrl = originalRequest.url();

                HttpUrl modifiedHttpUrl = originalHttpUrl.newBuilder()
                        .addQueryParameter("apiKey", BuildConfig.API_KEY)
                        .build();

                Request modifiedRequest = originalRequest.newBuilder()
                        .url(modifiedHttpUrl)
                        .build();

                return chain.proceed(modifiedRequest);
            }
        };

        //Create the http client with the interceptor
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        //Create the retrofit object and then the service API from it
        serviceAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ServiceAPI.class);
    }

    public static synchronized ServiceAPI getServiceAPI() {

        if(null == serviceAPI) {
            new RetrofitProvider();
        }

        return serviceAPI;
    }
}
