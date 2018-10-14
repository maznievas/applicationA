package com.example.andrey.applicationa;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.andrey.applicationa.database.AppDatabase;

public class App extends Application {

    public static App INSTANCE;
    AppDatabase appDatabase;

    public static App getApp(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, getString(R.string.databse_name)).build();
    }

    public AppDatabase getApppDatabase(){
        return appDatabase;
    }
}
