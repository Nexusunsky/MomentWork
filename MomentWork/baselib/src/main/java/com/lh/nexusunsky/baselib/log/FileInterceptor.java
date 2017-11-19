package com.lh.nexusunsky.baselib.log;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Nexusunsky
 */
public class FileInterceptor implements Interceptor {
    private static final String TAG = "FileInterceptor";
    private Executor runner = Executors.newSingleThreadExecutor();
    private boolean append;
    private FileCreator creator;
    private int level = LogLevel.FULL;

    private FileInterceptor(FileCreator creator, int level, boolean append) {
        this.creator = creator;
        this.level = level;
        this.append = append;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void log(final int type, final String tag, final String[] caller, final String message) {
        asyncWriteLog(false, tag, caller, message);
    }

    private void asyncWriteLog(final boolean needCrashMark, final String tag, final String[] caller, final String
            message) {
        runner.execute(new Runnable() {
            @Override
            public void run() {
                writeLogToFile(needCrashMark, tag, caller, message);
            }
        });
    }

    private void writeLogToFile(boolean needCrashMark, String tag, String[] caller, String message) {
        FileOutputStream fos = null;
        try {
            final String filePath = creator.getDirPath() + creator.getFileName(needCrashMark);

            if (tryToCreateDirPath(creator.getDirPath())) {

                File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                file.setLastModified(System.currentTimeMillis());

                fos = new FileOutputStream(file, append);
                String om = makeOutputMessage(level, tag, caller, message);

                if (Logger.DEBUG) {
                    Log.d(Logger.class.getSimpleName(), "write file : " + file.getAbsolutePath()
                            + Helper.LINE_SEPARATOR + "message : " + om);
                }

                fos.write(om.getBytes("UTF-8"));
                fos.flush();
            }

        } catch (Exception e) {
            Logger.e(TAG, e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.e(TAG, e);
                }
            }
        }
    }

    private static boolean tryToCreateDirPath(String dirPath) {
        File f = new File(dirPath);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }

    /**
     * 消息輸出的格式 <br/>
     * "2016-03-03 01:01:01.321 D/Tag callerInfos" <br/>
     * "message"
     */
    private static final String FORMAT = "%s %s/%s %s" + Helper.LINE_SEPARATOR + "%s" + Helper.LINE_SEPARATOR;

    protected String makeOutputMessage(int type, String tag, String[] caller, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        String time = dateFormat.format(new Date());
        String callerInfos = Helper.makeCallerInfos(caller);
        String levelName = getLevelName(type);
        return String.format(FORMAT, time, levelName, tag, callerInfos, message);
    }

    /**
     * 根据level输出具体的名字
     *
     * @param type
     * @return
     */
    private String getLevelName(int type) {
        String name;
        switch (type) {
            case LogLevel.VERBOSE:
                name = "V";
                break;
            case LogLevel.DEBUG:
                name = "D";
                break;
            case LogLevel.INFO:
                name = "I";
                break;
            case LogLevel.WARN:
                name = "W";
                break;
            case LogLevel.ERROR:
                name = "E";
                break;
            case LogLevel.ASSERT:
                name = "A";
                break;
            default:
                name = "UNKOWN";
                break;
        }
        return name;
    }

    public static class Builder {
        private FileCreator creator;
        private int level;
        private boolean append;

        public Builder fileCreator(FileCreator creator) {
            this.creator = creator;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        /**
         * 是否以追加方式写文件
         *
         * @param append
         */
        public Builder append(boolean append) {
            this.append = append;
            return this;
        }

        public FileInterceptor build() {
            return new FileInterceptor(this.creator, this.level, this.append);
        }
    }

    @Override
    public void crash(int level, final String tag, final String[] caller, final String message) {
        asyncWriteLog(true, tag, caller, message);
    }
}
