package com.isotne.glidelibrary.cache.util;

import com.isotne.glidelibrary.utils.LogUtils;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;


/**
 * description DiskLruCacheImpl
 *
 * @author baihe
 * created 2021/2/9 8:39
 */
public class DiskLruCacheImpl {
    /**
     * DISK_LRU_CACHE
     */
    private static DiskLruCache DISK_LRU_CACHE;
    /**
     * file
     */
    private File file;

    /**
     * @param path path
     * @throws IOException IOException
     */
    public DiskLruCacheImpl(String path) throws IOException {
        file = new File(path);
        if (DISK_LRU_CACHE == null) {
            DISK_LRU_CACHE = DiskLruCache.open(file, 1, 1, 1024 * 1024 * 10);
        }

    }

    /**
     * @param bytes bytes
     * @param key   key
     */
    public void addDiskCache(byte[] bytes, String key) {
        if (DISK_LRU_CACHE == null) {
            return;
        }
        try {
            DiskLruCache.Editor editor = DISK_LRU_CACHE.edit(CacheUtils.hashKeyForCache(key));
            if (editor != null) {

                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(bytes, 0, bytes.length);
                editor.commit();
                DISK_LRU_CACHE.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param key key
     * @return return
     */
    public PixelMap getDiskCache(String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = DISK_LRU_CACHE.get(CacheUtils.hashKeyForCache(key));
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
                    sourceOptions.formatHint = "image/png";
                    ImageSource imageSource = ImageSource.create(inputStream, sourceOptions);
                    PixelMap pixelMap = imageSource.createPixelmap(null);
                    return pixelMap;
                }
            }
        } catch (IOException e) {
            LogUtils.log(LogUtils.ERROR, "AAA", "e=" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param key key
     * @return if removed
     * @throws IOException IOException
     */
    public boolean removeDiskCach(String key) throws IOException {
        boolean isTrue = false;
        if (!isSet(CacheUtils.hashKeyForCache(key))) {
            isTrue = DISK_LRU_CACHE.remove(CacheUtils.hashKeyForCache(key));
        }
        return isTrue;
    }


    /**
     * @param key key
     * @return isset
     * @throws IOException IOException
     */
    public boolean isSet(String key) throws IOException {
        if (getDiskCache(CacheUtils.hashKeyForCache(key)) != null) {
            return false;
        }
        return true;
    }
}
