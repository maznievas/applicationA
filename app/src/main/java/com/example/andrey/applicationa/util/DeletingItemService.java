package com.example.andrey.applicationa.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.andrey.applicationa.App;
import com.example.andrey.applicationa.R;
import com.example.andrey.applicationa.database.ReferenceData;
import com.example.andrey.applicationa.database.ReferenceDataDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DeletingItemService extends Service {

    final String TAG = "mLog";
    private ReferenceDataDao referenceDataDao;
    ExecutorService es;
    private String stringExtraUrl;

    public void onCreate() {
        super.onCreate();
        referenceDataDao = App.getApp().getApppDatabase().referenceDataDao();
        es = Executors.newFixedThreadPool(1);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (intent != null) {
            stringExtraUrl = intent.getStringExtra(getString(R.string.url_extra));
        }
        deleteItemFromDatabase(stringExtraUrl, startId);
        return START_REDELIVER_INTENT;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    void deleteItemFromDatabase(String imageUrl, int startId) {
        Observable.timer(15, TimeUnit.SECONDS)
                .map(__ -> {
                    return new ReferenceData();
                })
                .map(referenceData -> {
                    referenceData.setUrl(imageUrl);
                    return referenceData;
                })
                .flatMapCompletable(referenceData -> Completable.fromAction(() -> {
                    Log.d(TAG, "deleteReference: " + referenceData.getUrl());
                    referenceDataDao.delete(referenceData);
                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Log.d("mLog", "Deleted");
                    stopSelf();
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(getString(R.string.receiver_delete_complete));
                    this.sendBroadcast(broadcastIntent);
                }, throwable -> {
                    Log.e(TAG, "deleting reference from database");
                    throwable.printStackTrace();
                });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
