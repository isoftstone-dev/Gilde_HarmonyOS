package com.isotne.glidelibrary.cache.util;

import ohos.aafwk.ability.AbilitySlice;
import ohos.app.Environment;
import ohos.media.image.ImagePacker;
import ohos.media.image.PixelMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * description ImageUtils
 *
 * @author baihe
 * created 2021/2/9 8:41
 */
public class ImageUtils {
    /**
     * @param abilitySlice abilitySlice
     * @param pixelMap     pixelMap
     * @param key          key
     */
    public static void savePixelMapToImage(AbilitySlice abilitySlice, PixelMap pixelMap, String key) {
        ImagePacker imagePacker = ImagePacker.create();
        File file = new File(abilitySlice.getExternalFilesDir(Environment.DIRECTORY_PICTURES), key + ".jpeg");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            ImagePacker.PackingOptions packingOptions = new ImagePacker.PackingOptions();
            packingOptions.format = "image/jpeg";
            packingOptions.quality = 100;// 设置图片质量
            boolean result = imagePacker.initializePacking(outputStream, packingOptions);
            result = imagePacker.addImage(pixelMap);
            long dataSize = imagePacker.finalizePacking();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * @param abilitySlice abilitySlice
     * @param key key
     * @return File
     */
    public static File getFile(AbilitySlice abilitySlice, String key) {
        File file = new File(abilitySlice.getExternalFilesDir(Environment.DIRECTORY_PICTURES), key + ".jpeg");
        return file;
    }


}
