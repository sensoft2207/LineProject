package com.mxi.lineapp.network;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by android on 18/5/17.
 */

public class AppControll extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
