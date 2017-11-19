package com.lh.nexusunsky.baselib.network.response;


import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Nexusunsky
 */
public abstract class JsonResponse implements IResponse {

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
            final Object result = new JSONTokener(finalResponseBodyStr).nextValue();
            if (result instanceof JSONObject) {
                AppContext.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONObject) result);
                    }
                });
            } else if (result instanceof JSONArray) {
                AppContext.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONArray) result);
                    }
                });
            } else {
                Logger.e("onResponse fail parse jsonobject, body=" + finalResponseBodyStr);
                AppContext.getMainHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(response.code(), "fail parse jsonobject, body=" + finalResponseBodyStr);
                    }
                });
            }
        } catch (JSONException e) {
            Logger.e("onResponse fail parse jsonobject, body=" + finalResponseBodyStr);
            AppContext.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse jsonobject, body=" + finalResponseBodyStr);
                }
            });
        }
    }

    public void onSuccess(int statusCode, JSONObject response) {
        Logger.w("onSuccess(int statusCode, JSONObject response) was not overriden, but callback was received");
    }

    public void onSuccess(int statusCode, JSONArray response) {
        Logger.w("onSuccess(int statusCode, JSONArray response) was not overriden, but callback was received");
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}