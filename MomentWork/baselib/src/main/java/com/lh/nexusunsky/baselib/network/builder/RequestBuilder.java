package com.lh.nexusunsky.baselib.network.builder;


import com.lh.nexusunsky.baselib.network.EasyHttp;
import com.lh.nexusunsky.baselib.network.response.IResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * @author Nexusunsky
 */
public abstract class RequestBuilder<T extends RequestBuilder> {
    protected String mUrl;
    protected Object mTag;
    protected Map<String, String> mHeaders;

    protected EasyHttp mEasyHttp;

    /**
     * 异步执行
     */
    abstract void enqueue(final IResponse responseHandler);

    public RequestBuilder(EasyHttp easyHttp) {
        mEasyHttp = easyHttp;
    }

    public T setUrl(String url) {
        this.mUrl = url;
        return (T) this;
    }

    public T tag(Object tag) {
        this.mTag = tag;
        return (T) this;
    }

    /**
     * 请求头对
     */
    public T headers(Map<String, String> headers) {
        this.mHeaders = headers;
        return (T) this;
    }

    /**
     * 单个请求头
     */
    public T addHeader(String key, String val) {
        if (this.mHeaders == null) {
            mHeaders = new LinkedHashMap<>();
        }
        mHeaders.put(key, val);
        return (T) this;
    }

    /**
     * 添加请求头参数
     */
    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }
}
