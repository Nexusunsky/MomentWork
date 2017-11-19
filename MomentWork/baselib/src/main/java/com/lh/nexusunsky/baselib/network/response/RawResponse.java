package com.lh.nexusunsky.baselib.network.response;


import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.log.Logger;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Nexusunsky
 */
public abstract class RawResponse implements IResponse {

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
        AppContext.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response.code(), finalResponseBodyStr);
            }
        });

    }

    public abstract void onSuccess(int statusCode, String response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
