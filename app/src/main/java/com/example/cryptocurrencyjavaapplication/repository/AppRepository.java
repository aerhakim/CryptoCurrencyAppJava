package com.example.cryptocurrencyjavaapplication.repository;

import android.util.Log;

import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.retrofit.RequestApi;
import com.example.cryptocurrencyjavaapplication.room.RoomDao;
import com.example.cryptocurrencyjavaapplication.room.entity.MarketListEntity;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppRepository {
    RequestApi requestApi;
    RoomDao roomDao;

    public AppRepository(RequestApi requestApi, RoomDao roomDao) {
        this.requestApi = requestApi;
        this.roomDao = roomDao;
    }

    public Future<Observable<AllMarketModel>> marketListFutureCall() {

        final ExecutorService executor = Executors.newSingleThreadExecutor();

        final Callable<Observable<AllMarketModel>> myNetworkCallable = new Callable<Observable<AllMarketModel>>() {
            @Override
            public Observable<AllMarketModel> call() throws Exception {
                return requestApi.makeMarketLastListCall();
            }
        };
        final Future<Observable<AllMarketModel>> futureObservable = new Future<Observable<AllMarketModel>>() {
            @Override
            public boolean cancel(boolean b) {
                if (b) {
                    executor.shutdown();
                }
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<AllMarketModel> get() throws ExecutionException, InterruptedException {
                return executor.submit(myNetworkCallable).get();
            }

            @Override
            public Observable<AllMarketModel> get(long timeOut, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(myNetworkCallable).get(timeOut, timeUnit);
            }
        };
        return futureObservable;
    }

    public void insertAllMarket(AllMarketModel allMarketModel){
        Completable.fromAction(() -> roomDao.insert(new MarketListEntity(allMarketModel)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("TAG", "onSubscribe: " );
                    }

                    @Override
                    public void onComplete() {
                        Log.e("TAG", "onComplete: " );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage() );
                    }
                });
    }

    public Flowable<MarketListEntity> getAllMarketData(){
        return roomDao.getAllMarketData();
    }




}