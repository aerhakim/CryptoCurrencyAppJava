package com.example.cryptocurrencyjavaapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cryptocurrencyjavaapplication.R;
import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.AllMarketModel;
import com.example.cryptocurrencyjavaapplication.repository.AppRepository;
import com.example.cryptocurrencyjavaapplication.room.entity.MarketListEntity;

import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<Integer>> mutableLiveData = new MutableLiveData<>();

    @Inject
    AppRepository appRepository;

    @Inject
    public AppViewModel(@NonNull Application application) {
        super(application);
        getViewPagerData();
    }
    public Future <Observable<AllMarketModel>> marketFutureCall(){
        return appRepository.marketListFutureCall();
    }

    public void insertAllMarket(AllMarketModel allMarketModel){
        appRepository.insertAllMarket(allMarketModel);
    }
    public Flowable<MarketListEntity> getAllMarketData(){
        return appRepository.getAllMarketData();
    }



    MutableLiveData<ArrayList<Integer>> getViewPagerData() {
        ArrayList<Integer> pics = new ArrayList<>();
        pics.add(R.drawable.p1);
        pics.add(R.drawable.p2);
        pics.add(R.drawable.p3);
        pics.add(R.drawable.p4);
        pics.add(R.drawable.p5);

        mutableLiveData.postValue(pics);
        return mutableLiveData;
    }

    // getter
    public MutableLiveData<ArrayList<Integer>> getMutableLiveData() {
        return mutableLiveData;
    }

}



