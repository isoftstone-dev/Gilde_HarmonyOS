package com.isotne.glidelibrary.cache.util;

import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.InputStream;
import java.io.File;
import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * description Utils
 *
 * @author baihe
 * created 2021/2/9 9:05
 */
public final class Utils {
    /**
     * US_ASCII
     */
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    /**
     * 编码格式UTF_8
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private Utils() {
    }

    /**
     * @param reader reader
     * @return String
     * @throws IOException IOException
     */
    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * @param dir file path
     * @throws IOException IOException
     */
    public static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }


    /**
     * @param closeable closeable
     */
    public static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * @param inputStream InputStream
     * @return PixelMap
     */
    public static PixelMap getStringStream(InputStream inputStream) {
        try {
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(inputStream, srcOpts);
            PixelMap pixelMap = imageSource.createPixelmap(new ImageSource.DecodingOptions());
            return pixelMap;

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }
}
