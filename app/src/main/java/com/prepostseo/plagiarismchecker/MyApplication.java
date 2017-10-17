package com.prepostseo.plagiarismchecker;

/**
 * Created by Hassan on 10/16/17.
 */

import android.app.Application;
import android.support.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}