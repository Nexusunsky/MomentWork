package com.lh.nexusunsky.baselib.network.builder;


import android.support.annotation.NonNull;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.network.callback.EasyCallback;
import com.lh.nexusunsky.baselib.network.response.IResponse;

import java.util.Map;

import okhttp3.Request;

/**
 * @author Nexusunsky
 */
public class GetRequest extends RequestWithParamBuilder<GetRequest> {

    public GetRequest(EasyHttp easyHttp) {
        super(easyHttp);
    }

    @Override
    public void enqueue(final IResponse responseHandler) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("setUrl can not be null !");
            }

            if (mParams != null && mParams.size() > 0) {
                mUrl = appendParams(mUrl, mParams);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();
            mEasyHttp.getOkHttpClient().
                    newCall(request).
                    enqueue(new EasyCallback(responseHandler));
        } catch (Exception e) {
            Logger.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to setUrl
    @NonNull
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
