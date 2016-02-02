package com.grapesnberries.aliabozaid.products.module;

import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.Environment;

import com.grapesnberries.aliabozaid.products.BuildConfig;
/*import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;*/

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
/*import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;*/
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.ScalarsConverterFactory;

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
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Customize the request
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                ///.header("Cache-Control", String.format("max-age=%d, only-if-cached, max-stale=%d", 432000, 0))
                                .removeHeader("Pragma")
                                .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                                .build();

                        Response response = chain.proceed(request);
                        response.cacheResponse();
                        // Customize or return the response
                        return response;
                    }
                })
                .cache(cache)

                .build();


        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
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
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR2 = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control",
                            String.format("max-age=%d", 60))
                    .build();
        }
    };

}
