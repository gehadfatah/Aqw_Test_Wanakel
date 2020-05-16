package com.example.weanaklie.core.application;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NotNull Context context, Glide glide, @NotNull Registry registry) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();

        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);

        glide.getRegistry().replace(GlideUrl.class, InputStream.class, factory);
    }

    @Override
    public void applyOptions(@NotNull Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.DEBUG);
        int diskCacheSizeBytes = 1024 * 1024 * 50; // 50 MB
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, "cacheGlide", diskCacheSizeBytes));

}
}
