package com.m.openthedoorapp.utils;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;


/*
created by mahmoud 10/4/2017
 */

public class AppController extends Application {

    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}