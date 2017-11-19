package com.lh.nexusunsky.baselib.log;

/**
 * @author Nexusunsky
 */
public interface FileCreator {
    /**
     * 获取文件名
     *
     * @param needCrashMark crash单独另加标记
     * @return
     */
    String getFileName(boolean needCrashMark);

    /**
     * 获取日志文件存放的路径
     *
     * @return
     */
    String getDirPath();
}
