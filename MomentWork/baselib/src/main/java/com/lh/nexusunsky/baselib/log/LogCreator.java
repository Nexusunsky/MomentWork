package com.lh.nexusunsky.baselib.log;

import android.content.Context;

/**
 * @author Nexusunsky
 */
public class LogCreator extends SimpleFileCreator {
    public LogCreator(Context context) {
        super(context);
    }

    @Override
    public String getLogDir() {
        return "nexus";
    }
}
