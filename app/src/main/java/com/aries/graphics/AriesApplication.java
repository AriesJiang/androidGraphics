package com.aries.graphics;

import android.app.Application;
import android.util.Log;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by JiangYiDong on 2018/8/2.
 */

public class AriesApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        Log.d("AriesApplication", "AriesApplication onCreate");
        // Normal app init code...
    }
}
