package com.lh.nexusunsky.baselib.network.builder;

import com.lh.nexusunsky.baselib.log.Logger;
import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.network.callback.EasyCallback;
import com.lh.nexusunsky.baselib.network.response.IResponse;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Nexusunsky
 */
public class PostRequest extends RequestWithParamBuilder<PostRequest> {

    private String mJsonParams = "";

    public PostRequest(EasyHttp easyHttp) {
        super(easyHttp);
    }

    /**
     * json格式参数
     */
    public PostRequest jsonParams(String json) {
        this.mJsonParams = json;
        return this;
    }

    @Override
    public void enqueue(IResponse responseHandler) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("setUrl can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            if (mJsonParams.length() > 0) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJsonParams);
                builder.post(body);
            } else {
                FormBody.Builder encodingBuilder = new FormBody.Builder();
                appendParams(encodingBuilder, mParams);
                builder.post(encodingBuilder.build());
            }
            Request request = builder.build();
            mEasyHttp.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new EasyCallback(responseHandler));
        } catch (Exception e) {
            Logger.e("Post enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    //append params to form builder

    /**
     * 添加参数到表单
     */
    private void appendParams(FormBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }
}
