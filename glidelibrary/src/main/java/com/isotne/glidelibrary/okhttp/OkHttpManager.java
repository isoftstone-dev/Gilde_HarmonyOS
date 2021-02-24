package com.isotne.glidelibrary.okhttp;

import com.isotne.glidelibrary.cache.util.DiskLruCacheUtil;
import com.isotne.glidelibrary.interceptor.NetCacheInterceptor;
import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

/**
 * description OKhttp manager
 *
 * @author baihe
 * created 2021/2/8 14:35
 */
public final class OkHttpManager {
    /**
     * client
     */
    private OkHttpClient client;

    private OkHttpManager() {
        client = new OkHttpClient.Builder()
                .cache(DiskLruCacheUtil.getCACHE()) // 配置缓存
                .addNetworkInterceptor(new NetCacheInterceptor())
                .build();
    }

    /**
     * getInstance
     *
     * @return OkHttpManager
     */
    public static OkHttpManager getInstance() {
        return OkHttpHolder.INSTANCE;
    }

    /**
     * OkHttpHolder
     */
    private static class OkHttpHolder {
        /**
         * OkHttpManager 单例
         */
        private static final OkHttpManager INSTANCE = new OkHttpManager();
    }

    /**
     * @param callback callback
     * @param url      url
     */
    public void asyncGet(Callback callback, String url) {
        CacheControl cacheControl = new CacheControl.Builder()
                .maxStale(10000, TimeUnit.SECONDS)
                .maxAge(100000, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl)
                .build();

        client.newCall(request).enqueue(callback);
    }
}