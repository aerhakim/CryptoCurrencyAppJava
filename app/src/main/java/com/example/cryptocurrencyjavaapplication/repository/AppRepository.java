package com.example.cryptocurrencyjavaapplication.repository;

import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.retrofit.RequestApi;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.core.Observable;

public class AppRepository {
    RequestApi requestApi;

    public AppRepository(RequestApi requestApi) {
        this.requestApi = requestApi;
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
}