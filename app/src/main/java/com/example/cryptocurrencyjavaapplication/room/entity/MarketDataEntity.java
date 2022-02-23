package com.example.cryptocurrencyjavaapplication.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cryptocurrencyjavaapplication.models.cryptolistmodel.CryptoMarketDataModel;


@Entity(tableName = "Crypto")
public class MarketDataEntity {

    @PrimaryKey
    public int uid;

    public int getUid() {
        return uid;
    }

    public CryptoMarketDataModel getCryptoMarketModel() {
        return cryptoMarketDataModel;
    }

    @ColumnInfo(name = "CryptoData")
    public CryptoMarketDataModel cryptoMarketDataModel;


    public MarketDataEntity(CryptoMarketDataModel cryptoMarketDataModel) {
        this.cryptoMarketDataModel = cryptoMarketDataModel;
    }
}
