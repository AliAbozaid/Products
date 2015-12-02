package com.grapesnberries.aliabozaid.products.module;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Environment;

import com.grapesnberries.aliabozaid.products.BuildConfig;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by aliabozaid on 11/28/15.
 */
@Module
public class ApiModule {

    /*@Provides
    @Singleton
    Context getContext(){
        return getContext();
    }*/
    File cacheFile;
    public ApiModule(File cacheFile)
    {
        this.cacheFile = cacheFile;
    }
    @Provides
    @Singleton
    Retrofit provideCall()
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.networkInterceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        okHttpClient.setCache(cache);
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", BuildConfig.CACHETIME, 0))
                    .build();
        }
    };

}
