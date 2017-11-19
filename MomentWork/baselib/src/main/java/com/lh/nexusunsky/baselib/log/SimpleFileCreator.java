package com.lh.nexusunsky.baselib.log;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.format.DateFormat;

import java.io.File;

/**
 * @author Nexusunsky
 */
public class SimpleFileCreator implements FileCreator {

    private final String SUFFIX = ".log";
    private static final String CRASH_MARK = "-crash";

    private Context applicationContext;

    public SimpleFileCreator(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    @Override
    public String getFileName(boolean needCrashMark) {
        final String crashMark = needCrashMark ? CRASH_MARK : "";
        return DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()) + crashMark + SUFFIX;
    }

    @Override
    public String getDirPath() {
        if (checkSDcard() && hasExternalStoragePermission(getContext())) {
            return getExternalStorageDir();
        } else {
            return getInternalStorageDir();
        }
    }

    protected Context getContext() {
        return this.applicationContext;
    }

    protected String getInternalStorageDir() {
        return "/data/data/" + getLogDir() + "/file/log/";
    }

    protected String getExternalStorageDir() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + getLogDir()
                + "/log/";
    }

    protected String getLogDir() {
        return getContext().getPackageName();
    }

    /**
     * 检测SD卡是否存在
     */
    private boolean checkSDcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 外部存储器访问权限
     */
    private final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
