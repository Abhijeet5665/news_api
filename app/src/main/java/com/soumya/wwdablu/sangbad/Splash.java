package com.soumya.wwdablu.sangbad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Splash extends AppCompatActivity {

    private static final int DELAY = 3000;
    private DisposableObserver disposableObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        disposableObserver = Observable.timer(DELAY, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                launchNextActivity();
                finish();
                disposableObserver.dispose();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposableObserver.dispose();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //This ensures that the back button is consumed
    }

    private void launchNextActivity() {

        Intent intent = new Intent(Splash.this, SourcesActivity.class);
        startActivity(intent);
    }
}
