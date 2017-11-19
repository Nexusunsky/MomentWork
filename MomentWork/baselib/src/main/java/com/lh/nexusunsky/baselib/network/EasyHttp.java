package com.lh.nexusunsky.baselib.network;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.builder.GetRequest;
import com.lh.nexusunsky.baselib.network.builder.PostRequest;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author Nexusunsky
 */
public class EasyHttp {
    private static final String TAG = EasyHttp.class.getSimpleName();
    private OkHttpClient mOkHttpClient;

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private EasyHttp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(logging)
                .build();
    }

    @Contract(pure = true)
    public static EasyHttp getHttp() {
        return SingletonHolder.INSTANCE;
    }

    public static void init() {
        Logger.d(TAG, SingletonHolder.INSTANCE);
    }

    public GetRequest get() {
        return new GetRequest(this);
    }

    public PostRequest post() {
        return new PostRequest(this);
    }

    /**
     * 停止执行中的请求
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    @Override
    public String toString() {
        return "EasyHttp{" +
                "mOkHttpClient=" + mOkHttpClient +
                '}';
    }

    private static class SingletonHolder {
        private SingletonHolder() {
        }

        private static final EasyHttp INSTANCE = new EasyHttp();
    }
}
