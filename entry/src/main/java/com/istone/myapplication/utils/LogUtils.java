package com.istone.myapplication.utils;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * description logutil
 *
 * @author baihe
 * created 2021/2/8 14:52
 */
public class LogUtils {
    /**
     * info
     */
    public static final int INFO = 0;
    /**
     * error
     */
    public static final int ERROR = 1;
    /**
     * debug
     */
    public static final int DEBUG = 2;
    /**
     * warning
     */
    public static final int WARN = 3;

    /**
     * @param logType LogUtils.INFO || LogUtils.ERROR || LogUtils.DEBUG|| LogUtils.WARN
     * @param tag     日志标识  根据喜好，自定义
     * @param message 需要打印的日志信息
     */
    public static void log(int logType, String tag, String message) {
        HiLogLabel lable = new HiLogLabel(HiLog.LOG_APP, 0x0, tag);
        switch (logType) {
            case INFO:
                HiLog.info(lable, message);
                break;
            case ERROR:
                HiLog.error(lable, message);
                break;
            case DEBUG:
                HiLog.debug(lable, message);
                break;
            case WARN:
                HiLog.warn(lable, message);
                break;
            default:
                break;

        }
    }


}
