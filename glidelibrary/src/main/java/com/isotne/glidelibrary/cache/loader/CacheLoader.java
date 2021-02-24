package com.isotne.glidelibrary.cache.loader;

import com.bumptech.glide.util.LruCache;
import ohos.media.image.PixelMap;

/**
 * description CacheLoader
 *
 * @author baihe
 * created 2021/2/9 8:41
 */
public class CacheLoader {
    /**
     * lruCache
     */
    private LruCache<String, PixelMap> lruCache;

    /**
     * CacheLoader
     */
    public CacheLoader() {
        //设置最大缓存空间为运行时内存的 1/8
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<>(cacheSize);

    }

    /**
     * 添加图片到 LruCache
     *
     * @param key      key
     * @param pixelMap pixelMap
     */
    public void addBitmap(String key, PixelMap pixelMap) {
        if (getPixelMap(key) == null) {
            lruCache.put(key, pixelMap);
        }
    }

    /**
     * 从缓存中获取资源
     *
     * @param key key
     * @return PixelMap
     */
    public PixelMap getPixelMap(String key) {
        return lruCache.get(key);
    }

    /**
     * 从缓存中删除指定的PixelMap
     *
     * @param key key
     */
    public void removePixelMapFromMemory(String key) {
        lruCache.remove(key);
    }
}
