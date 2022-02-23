package com.example.cryptocurrencyjavaapplication.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cryptocurrencyjavaapplication.room.entity.MarketDataEntity;
import com.example.cryptocurrencyjavaapplication.room.entity.MarketListEntity;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketDataEntity marketDataEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MarketListEntity marketListEntity);

    @Query("SELECT * FROM AllMarket")
    Flowable<MarketListEntity> getAllMarketData();

    @Query("SELECT * FROM Crypto")
    Flowable<MarketDataEntity> getCryptoMarketData();


}
