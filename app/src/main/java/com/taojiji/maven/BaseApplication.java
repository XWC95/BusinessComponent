package com.taojiji.maven;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author Vea
 * @version VERSION
 * @since 2019-01-04
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
