package com.isotne.glidelibrary.utils;

import com.isotne.glidelibrary.FileType;
import com.isotne.glidelibrary.cache.util.DiskLruCacheImpl;
import com.isotne.glidelibrary.cache.util.MemoryCacheUtil;
import com.isotne.glidelibrary.constant.Constents;
import com.isotne.glidelibrary.gif.LoadGif;
import com.isotne.glidelibrary.okhttp.OkHttpManager;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.Image;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * description 鸿蒙 Glide 组件
 *
 * @author baihe
 * created 2021/2/8 11:54
 */
public class OhosGlide {
    /**
     * abilitySlice
     */
    private AbilitySlice abilitySlice;
    /**
     * url 图片地址
     */
    private String url = null;
    /**
     * inputstream
     */
    private InputStream inputStream;
    /**
     * default imager resources id
     */
    private int defImage;
    /**
     * iamge
     */
    private Image image;
    /**
     * whole image
     */
    private PixelMap pixelMap;
    /**
     * file  type
     */
    private FileType fileType;
    /**
     * diskLruCacheImpl
     */
    private DiskLruCacheImpl diskLruCacheImpl;
    /**
     * pixelmap list
     */
    private List<PixelMap> pixelMapList = new ArrayList<>();
    /**
     * animatorvalue
     */
    private AnimatorValue animatorValue;

    /**
     * init disk
     *
     * @param ability
     * @throws IOException
     */
    public OhosGlide(AbilitySlice ability) throws IOException {
        this.abilitySlice = ability;
        if (diskLruCacheImpl == null) {
            diskLruCacheImpl =
                    new DiskLruCacheImpl(abilitySlice.getExternalCacheDir().toString() + "/" + Constents.DISK_CACHE_PATH);

        }

    }



    /**
     * init glide
     *
     * @param ability ability
     * @return OhosGlide
     * @throws IOException IOException
     */
    public static OhosGlide with(AbilitySlice ability) throws IOException {

        return new OhosGlide(ability);
    }

    /**
     * load url
     *
     * @param url url
     * @return OhosGlide
     */
    public OhosGlide load(String url) {
        this.url = url;
        return this;
    }

    /**
     * load inputstream
     *
     * @param inputStream inputStream
     * @return OhosGlide
     */
    public OhosGlide load(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    /**
     * 设置默认图片，当发生异常时使用
     *
     * @param defImage defImage
     * @return OhosGlide
     */
    public OhosGlide def(int defImage) {
        this.defImage = defImage;
        return this;
    }

    /**
     * if set cache
     *
     * @param isDiskCache isDiskCache
     * @return OhosGlide
     */
    public OhosGlide hasDiskCache(boolean isDiskCache) {
        return this;
    }

    /**
     * 需要渲染的iamge
     *
     * @param image image
     */
    public void into(Image image) {
        this.image = image;
        if (url == null) {
            setLoalImage(inputStream);
        } else {
            if (url.contains(".png") || url.contains(".jpg") || url.contains(".jpeg") || url.contains(".bmp")) {
                fileType = FileType.PNG;
            } else if (url.contains(".gif")) {
                fileType = FileType.GIF;
            }
            boolean hasCache = MemoryCacheUtil.isCache(url);
            if (hasCache) {//内存有缓存
                abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
                    PixelMap pixelMap = MemoryCacheUtil.getPixelMap(url);
                    image.setPixelMap(pixelMap);
                });

            } else {
                pixelMap = diskLruCacheImpl.getDiskCache(url);
                if (pixelMap != null) {//磁盘有缓存
                    abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
                        image.setPixelMap(pixelMap);
                    });
                } else {//从网络下载file
                    downLoadFile();
                }
            }
        }
    }

    /**
     * downLoadFile
     */
    public void downLoadFile() {
        if (image == null) {
            image.setPixelMap(defImage);
        }

        OkHttpManager.getInstance().asyncGet(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                pixelMap = diskLruCacheImpl.getDiskCache(url);
                if (pixelMap != null) {
                    abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
                        image.setPixelMap(pixelMap);
                    });
                } else {
                    abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
                        image.setPixelMap(defImage);
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                byte[] bytes = response.body().bytes();
                if (response.isSuccessful()) {
                    if (fileType == FileType.PNG) {
                        showImage(bytes);
                    } else if (fileType == FileType.GIF) {
                        showGif(response.body().byteStream());
                    }
                    diskLruCacheImpl.addDiskCache(bytes, url);
                }
            }
        }, url);

    }

    /**
     * 展示网络图片
     *
     * @param bytes bytes
     */
    private void showImage(byte[] bytes) {
//        File file = abilitySlice.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        convertToFile(file.getAbsolutePath(), inputStream);
//        File imgFile = new File(file.getAbsolutePath(), "test.png");
        ImageSource imageSource = ImageSource.create(bytes, new ImageSource.SourceOptions());
        pixelMap = imageSource.createPixelmap(new ImageSource.DecodingOptions());

        /**添加缩略图
         ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
         PixelMap thumbnailPixelmap = imageSource.createThumbnailPixelmap(decodingOpts, false);
         **/

        abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {

            image.setPixelMap(pixelMap);
            MemoryCacheUtil.savePixelMap(url, pixelMap);


        });
    }


    /**
     * 展示gif
     */
    private int index = 0;

    /**
     *
     * @param inputStream inputStream
     */
    private void showGif(InputStream inputStream) {

        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        decodingOpts.allowPartialImage = true;
        sourceOptions.formatHint = "image/gif";
        ImageSource imageSource = ImageSource.create(inputStream, sourceOptions);

        if (imageSource != null) {
            index = 0;
            while (imageSource.createPixelmap(index, decodingOpts) != null) {
                pixelMapList.add(imageSource.createPixelmap(index, decodingOpts));
                index++;
            }
        }
        // start anim
        abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
            LoadGif.loadGif(abilitySlice, pixelMapList, image, index);
        });
    }


    /**
     * 展示本地图片
     *
     * @param inputStream inputStream
     */
    private void setLoalImage(InputStream inputStream) {
        ImageSource imageSource = ImageSource.create(inputStream, new ImageSource.SourceOptions());
        PixelMap pixelMap = imageSource.createPixelmap(new ImageSource.DecodingOptions());
        abilitySlice.getUITaskDispatcher().asyncDispatch(() -> {
            image.setPixelMap(pixelMap);
        });
    }

    /**
     * 将inputstream 转换为图片
     *
     * @param path        path
     * @param inputStream inputStream
     */
    private void convertToFile(String path, InputStream inputStream) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "test.png");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            try {
                byte[] b = new byte[1024];
                int n;
                while ((n = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, n);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}


