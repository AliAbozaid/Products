package com.grapesnberries.aliabozaid.products.singleton;

import android.app.Application;
import android.util.DisplayMetrics;

import com.grapesnberries.aliabozaid.products.contollers.ApiComponent;
import com.grapesnberries.aliabozaid.products.module.ApiModule;
import com.grapesnberries.aliabozaid.products.contollers.DaggerApiComponent;

import java.io.File;

/**
 * Created by aliabozaid on 11/26/15.
 */
public class MyApplication extends Application {

    ApiComponent apiComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        File cacheFile = new File(getCacheDir(), "responses");
        apiComponent = DaggerApiComponent.builder().apiModule(new ApiModule(cacheFile)).build();

    }

    public ApiComponent getApiComponent()
    {
        return apiComponent;
    }

}
