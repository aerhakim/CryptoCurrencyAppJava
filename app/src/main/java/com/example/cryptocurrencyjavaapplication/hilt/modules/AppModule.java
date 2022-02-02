package com.example.cryptocurrencyjavaapplication.hilt.modules;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class AppModule {

    @Provides
    public String getName(){
        return "Morteza";
    }
}
