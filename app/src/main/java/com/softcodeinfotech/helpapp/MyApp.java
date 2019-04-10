package com.softcodeinfotech.helpapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.softcodeinfotech.helpapp.util.SharePreferenceUtils;

import java.util.Locale;

public class MyApp extends Application {

    private static Context context;
    private String TAG = "myApp";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        String languageToLoad  = SharePreferenceUtils.getInstance().getString("lang"); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());




        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

    }

    public static Context getContext() {
        return context;
    }


}
