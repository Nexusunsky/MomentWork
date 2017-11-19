package com.lh.nexusunsky.baselib.network.response;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.log.Logger;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Nexusunsky
 */
public abstract class GsonResponse<T> implements IResponse {

    private Type mType;

    public GsonResponse() {
        Type clazz = getClass().getGenericSuperclass();
        if (clazz instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) clazz;
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

    private Type getType() {
        return mType;
    }

    @Override
    public final void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";

        try {
            responseBodyStr = responseBody.string();
        } catch (IOException e) {
            Logger.e("onResponse fail read response body");
            AppContext.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail read response body");
                }
            });
            return;
        } finally {
            responseBody.close();
        }

        final String finalResponseBodyStr = responseBodyStr;

        try {
            AppContext.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    onSuccess(response.code(), (T) gson.fromJson(finalResponseBodyStr, getType()));
                }
            });
        } catch (Exception e) {
            Logger.e("onResponse fail parse gson, body=" + finalResponseBodyStr);
            AppContext.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse gson, body=" + finalResponseBodyStr);
                }
            });
        }
    }

    /**
     * Success Response
     *
     * @param statusCode
     * @param response
     * @return
     */
    public abstract void onSuccess(int statusCode, T response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
