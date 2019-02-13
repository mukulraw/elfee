package com.softcodeinfotech.helpapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApp extends Application {

    private static Context context;
    private String TAG = "myApp";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

    }

    public static Context getContext() {
        return context;
    }
}
