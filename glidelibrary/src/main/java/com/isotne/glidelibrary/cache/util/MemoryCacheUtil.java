package com.isotne.glidelibrary.cache.util;

import com.isotne.glidelibrary.cache.loader.CacheLoader;
import ohos.media.image.PixelMap;

/**
 * description
 *
 * @author baihe
 * created 2021/2/8 11:52
 */
public class MemoryCacheUtil {
    /**
     * CACHE_LOADER
     */
    private static CacheLoader CACHE_LOADER = new CacheLoader();

    /**
     * @param key key
     * @return if has key
     */
    public static boolean isCache(String key) {
        if (CACHE_LOADER.getPixelMap(CacheUtils.hashKeyForCache(key)) != null) {
            return true;
        }
        return false;
    }

    /**
     * @param key      key
     * @param pixelMap pixelMap
     */
    public static void savePixelMap(String key, PixelMap pixelMap) {
        if (!isCache(key)) {
            CACHE_LOADER.addBitmap(CacheUtils.hashKeyForCache(key), pixelMap);
        }
    }

    /**
     * @param key key
     */
    public static void cleanCache(String key) {
        if (isCache(key)) {
            CACHE_LOADER.removePixelMapFromMemory(CacheUtils.hashKeyForCache(key));
        }
    }

    /**
     * @param key key
     * @return PixelMap
     */
    public static PixelMap getPixelMap(String key) {
        return CACHE_LOADER.getPixelMap(CacheUtils.hashKeyForCache(key));

    }
}
