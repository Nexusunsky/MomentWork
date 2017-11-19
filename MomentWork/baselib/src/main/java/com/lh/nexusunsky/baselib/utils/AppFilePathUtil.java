package com.lh.nexusunsky.baselib.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.log.Logger;

import java.io.File;


/**
 * @author Nexusunsky
 */
public class AppFilePathUtil {
    private static final String TAG = "AppFileHelper";

    public static final String[] INTERNAL_STORAGE_PATHS = new String[]{"/mnt/", "/emmc/"};
    public static final String ROOT_PATH = "com/lh/momentwork/";
    public static final String DATA_PATH = ROOT_PATH + ".data/";
    public static final String CACHE_PATH = ROOT_PATH + ".cache/";
    public static final String PIC_PATH = ROOT_PATH + ".pic/.nomedia/";
    public static final String LOG_PATH = ROOT_PATH + ".log/";
    public static final String TEMP_PATH = ROOT_PATH + ".temp/";

    private static String storagePath;

    public static void initStoryPath() {
        if (TextUtils.isEmpty(storagePath)) {
            //storagePath = FileUtil.getStoragePath(AppContext.getAppContext(), FileUtil.hasSDCard());
            //因为外置sd卡似乎无法写入，所以写到内置sd卡
            storagePath = FileUtil.getStoragePath(AppContext.getAppContext(), false);
            if (TextUtils.isEmpty(storagePath)) {
                storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (TextUtils.isEmpty(storagePath)) {
                    storagePath = AppContext.getAppContext().getFilesDir().getAbsolutePath();
                }
            }
        }

        storagePath = FileUtil.checkFileSeparator(storagePath);
        Logger.i(TAG, "storagepath  >>  " + storagePath);

        File rootDir = new File(storagePath.concat(ROOT_PATH));
        checkAndMakeDir(rootDir);

        File dataDir = new File(storagePath.concat(DATA_PATH));
        checkAndMakeDir(dataDir);

        File cacheDir = new File(storagePath.concat(CACHE_PATH));
        checkAndMakeDir(cacheDir);

        File picDir = new File(storagePath.concat(PIC_PATH));
        checkAndMakeDir(picDir);

        File logDir = new File(storagePath.concat(LOG_PATH));
        checkAndMakeDir(logDir);

        File tempDir = new File(storagePath.concat(TEMP_PATH));
        checkAndMakeDir(tempDir);

    }

    private static void checkAndMakeDir(File file) {
        if (!file.exists()) {
            Logger.i("mkdirs  >>>  " + file.getAbsolutePath());
            file.mkdirs();
        }
    }

    public static String getAppCachePath() {
        return storagePath.concat(CACHE_PATH);
    }
}
