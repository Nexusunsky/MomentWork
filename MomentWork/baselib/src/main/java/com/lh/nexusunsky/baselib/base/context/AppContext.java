package com.lh.nexusunsky.baselib.base.context;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.lh.nexusunsky.baselib.log.ConsoleInterceptor;
import com.lh.nexusunsky.baselib.log.FileCreator;
import com.lh.nexusunsky.baselib.log.FileInterceptor;
import com.lh.nexusunsky.baselib.log.LogCreator;
import com.lh.nexusunsky.baselib.log.LogLevel;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.utils.AppFilePathUtil;


/**
 * @author Nexusunsky
 */
public class AppContext extends Application {

    private static final String TAG = AppContext.class.getSimpleName();
    private static final InnerLifecycleHandler INNER_LIFECYCLE_HANDLER;
    private static Handler mainHandler;
    private volatile LogCreator logCreator;
    private static AppContext appInstance;

    static {
        INNER_LIFECYCLE_HANDLER = new InnerLifecycleHandler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (AppContext.class) {
            appInstance = this;
        }
        registerActivityLifecycleCallbacks(INNER_LIFECYCLE_HANDLER);
        initLog();
        EasyHttp.init();
        AppFilePathUtil.initStoryPath();
    }

    private void initLog() {
        FileInterceptor fileInterceptor =
                new FileInterceptor.Builder().append(true).level(LogLevel.DEBUG).fileCreator(getFileCreator()).build();
        Logger.init().tag(TAG).add(new ConsoleInterceptor(LogLevel.DEBUG)).add(fileInterceptor);
    }

    protected FileCreator getFileCreator() {
        if (logCreator == null) {
            synchronized (AppContext.class) {
                if (logCreator == null) {
                    logCreator = new LogCreator(getApplicationContext());
                }
            }
        }
        return logCreator;
    }

    public static boolean isAppVisible() {
        checkAppContext();
        return INNER_LIFECYCLE_HANDLER != null && INNER_LIFECYCLE_HANDLER.started > INNER_LIFECYCLE_HANDLER.stopped;
    }

    public static boolean isAppBackground() {
        checkAppContext();
        return INNER_LIFECYCLE_HANDLER != null && INNER_LIFECYCLE_HANDLER.paused > INNER_LIFECYCLE_HANDLER.resumed;
    }

    public static Application getAppInstance() {
        checkAppContext();
        return appInstance;
    }

    public static Context getAppContext() {
        checkAppContext();
        return appInstance.getApplicationContext();
    }

    public static Resources getResource() {
        checkAppContext();
        return getAppContext().getResources();
    }

    /**
     * 获取主线成的handler，主线成唯一，维护消息队列的handler可以直接从这里，一般给bussiness层去使用
     *
     * @return handler
     */
    public static Handler getMainHandler() {
        if (mainHandler != null) {
            return mainHandler;
        }
        synchronized (AppContext.class) {
            if (mainHandler == null) {
                mainHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mainHandler;
    }

    private static void checkAppContext() {
        if (appInstance == null) {
            throw new IllegalStateException("app reference is null");
        }
    }

    private static class InnerLifecycleHandler implements Application.ActivityLifecycleCallbacks {
        private int created;
        private int resumed;
        private int paused;
        private int started;
        private int stopped;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ++created;
        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            ++resumed;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            ++paused;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
