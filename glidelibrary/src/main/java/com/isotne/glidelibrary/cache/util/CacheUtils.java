package com.isotne.glidelibrary.cache.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * description CacheUtils
 *
 * @author baihe
 * created 2021/2/9 8:41
 */
public class CacheUtils {

    /**
     * 将用于缓存的key转成MD5字符串，以确保唯一性
     *
     * @param key key
     * @return String
     */
    public static String hashKeyForCache(String key) {
        if (key != null) {
            String cacheKey;
            try {
                final MessageDigest mDigest = MessageDigest.getInstance("MD5");
                mDigest.update(key.getBytes());
                cacheKey = bytesToHexString(mDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                cacheKey = String.valueOf(key.hashCode());
            }
            return cacheKey;
        }
        return null;
    }

    /**
     * @param bytes bytes
     * @return String
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
