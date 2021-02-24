package com.isotne.glidelibrary.cache.util;

import com.isotne.glidelibrary.constant.Constents;
import ohos.aafwk.ability.AbilitySlice;
import okhttp3.Cache;

import java.io.File;

/**
 * description DiskLruCacheUtil
 *
 * @author baihe
 * created 2021/2/9 8:41
 */
public class DiskLruCacheUtil {
    /**
     * getCache
     *
     * @return cache
     */
    public static Cache getCACHE() {
        return DiskLruCacheUtil.CACHE;
    }

    /**
     * setCache
     *
     * @param cache cache
     */
    public static void setCACHE(Cache cache) {
        DiskLruCacheUtil.CACHE = cache;
    }

    /**
     * cache
     */
    private static Cache CACHE;

    /**
     * @param abilitySlice abilitySlice
     */
    public DiskLruCacheUtil(AbilitySlice abilitySlice) {
        File file = new File(abilitySlice.getExternalCacheDir().toString(), Constents.DISK_CACHE_PATH);
        CACHE = new Cache(file, Constents.DISK_CACHE_SIZE);
        setCACHE(CACHE);
    }


}
