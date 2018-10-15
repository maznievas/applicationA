package com.example.andrey.applicationa.main;

import android.arch.persistence.room.Room;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.andrey.applicationa.App;
import com.example.andrey.applicationa.database.AppDatabase;
import com.example.andrey.applicationa.database.ReferenceData;
import com.example.andrey.applicationa.database.ReferenceDataDao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private CompositeDisposable compositeDisposable;
    private ReferenceDataDao referenceDataDao;
    private final String TAG = "mLog";

    public MainPresenter() {
        compositeDisposable = new CompositeDisposable();
        referenceDataDao = App.getApp().getApppDatabase().referenceDataDao();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        showReferencesList();
    }

    public void testDataBase() {
        List<ReferenceData> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ReferenceData referenceData = new ReferenceData();
            referenceData.setUrl(String.valueOf("Url" + i));
            referenceData.setStatus(i);
            referenceData.setLastOpened(Calendar.getInstance().getTime());

            list.add(referenceData);
        }
        compositeDisposable.add(
//                Flowable.fromIterable(list)
//                .flatMapCompletable(referenceData ->
//                        Completable.fromAction(() -> {
//                            referenceDataDao.insert(referenceData);
//                    })
//                                .doOnComplete(() -> Log.d(TAG, "Inserting complete"))
//                        .doOnError(c -> {
//                            Log.d(TAG, "Insert onErrorlll");
//                        })
//                )
//                        .andThen(getAllReferences())
//                .toFlowable()
//                .flatMap(__ -> {
//                    return getAllReferences();
//                })
                getAllReferences()
//                .flatMapIterable(allReferences -> allReferences)
//                .flatMapCompletable(referenceData ->
//                    Completable.fromAction(() -> {
//                      referenceDataDao.delete(referenceData);
//                    })
//                )
//                .toFlowable()
//                .flatMap(__ -> {
//                    return getAllReferences();
//                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(allRefs -> {
                            getViewState().updateReferencesList(allRefs);
                        }, throwable -> {
                            Log.d(TAG, "database error");
                            throwable.printStackTrace();
                        })
        );
    }

    public void saveReference(ReferenceData referenceData) {
        compositeDisposable.add(
                Completable.fromAction(() -> {
                    referenceDataDao.insert(referenceData);
                })
                        .andThen(getAllReferences())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> getViewState().showLoadingState(true))
                        .doAfterTerminate(() -> getViewState().showLoadingState(false))
                        .subscribe(referenceDataList -> {
                                    Log.d(TAG, "successfully inserted");
                                    getViewState().setReferencesList(referenceDataList);
                                },
                                throwable -> {
                                    Log.e(TAG, "Error while inserting");
                                    throwable.printStackTrace();
                                })
        );
    }

    public void showReferencesList() {
        compositeDisposable.add(
                getAllReferences()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(c -> getViewState().showLoadingState(true))
                        .doAfterTerminate(() -> getViewState().showLoadingState(false))
                        .subscribe(referenceDataList -> {
                            getViewState().setReferencesList(referenceDataList);
                        }, throwable -> {
                            Log.e(TAG, "Error while loading references from database");
                            throwable.printStackTrace();
                        })
        );
    }

    public Flowable<List<ReferenceData>> getAllReferences() {
        return Flowable.create(emitter -> {
            List<ReferenceData> allReferenes = referenceDataDao.getAllReferenes();
            for (ReferenceData ref : allReferenes)
                Log.d(TAG, "url: " + ref.getUrl() + " status: " + ref.getStatus() + " date: " + ref.getLastOpened());
            try {
                emitter.onNext(allReferenes);
                emitter.onComplete();
            } catch (Throwable e) {
                emitter.onError(e);
            }
        }, BackpressureStrategy.BUFFER);
    }

    public void clear() {
        compositeDisposable.clear();
    }

    public void deleteReference(ReferenceData referenceData) {
        compositeDisposable.add(
                Observable.timer(15, TimeUnit.SECONDS)
                        .flatMapCompletable(__ -> Completable.fromAction(() -> {
                            referenceDataDao.delete(referenceData);
                        }))
                        .andThen(getAllReferences())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
//                        .doOnSubscribe(c -> getViewState().showLoadingState(true))
//                        .doAfterTerminate(() -> getViewState().showLoadingState(false))
                        .subscribe(referenceDataList -> {
                            Log.d("mLog", "Deleted");
                            getViewState().setReferencesList(referenceDataList);
                        }, throwable -> {
                            Log.e(TAG, "deleting reference from database");
                            throwable.printStackTrace();
                        })
        );
    }
}
