package com.lh.nexusunsky.baselib.network.callback;

import com.lh.nexusunsky.baselib.base.context.AppContext;
import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.response.IResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author Nexusunsky
 */
public class EasyCallback implements Callback {

    private IResponse mResponseHandler;

    public EasyCallback(IResponse responseHandler) {
        mResponseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        Logger.e("onFailure", e);

        AppContext.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(0, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) {
        if (response.isSuccessful()) {
            mResponseHandler.onSuccess(response);
        } else {
            Logger.e("onResponse fail status=" + response.code());

            AppContext.getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(response.code(), "fail status=" + response.code());
                }
            });
        }
    }
}
